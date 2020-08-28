package com.npn.spring.learning.spring.smallspringboot.model.html.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlNavElement;
import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlNavElementServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.repositories.HtmlNavElementsRepository;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Служба, для работы с элементом меню
 */
@Service
public class HtmlNavElementService implements HtmlNavElementServiceInterface {

    /**
     * Срок обновления кэша, в милисекундах
     */
    private static final Long CACHE_UPDATING_PERIOD = 20*60*1000L;

    /**
     * Объект локер
     */
    private static final ReentrantReadWriteLock isUpdate  = new ReentrantReadWriteLock();

    /**
     * Кэшированные записи из БД упорядоченные по ElementOrder
     */
    private static final CopyOnWriteArrayList<HtmlNavElement> cacheList = new CopyOnWriteArrayList<>();

    /**
     * Требуется ли обновление кэша, после выполнения операций добавления/обновления ....
     */
    private static volatile boolean needUpdate = true;

    /**
     * Последняя дата обновления
     */
    private static volatile Date lasUpdatingDate = null;


    private HtmlNavElementsRepository repository;

    /**
     * Находит корневые элементы, отдает отсортиованную по ElementOrder копию списка
     * @return List<HtmlNavElement>
     */
    private List<HtmlNavElement> findByParentIsNullOrderByElementOrderCached() {
        updateCache();
        isUpdate.readLock().lock();
        try {
            return cacheList.stream().filter(x->x.getParent()==null).collect(Collectors.toList());
        } finally {
            isUpdate.readLock().unlock();
        }

    }

    /**
     * Находит элемент по Id
     * @param id id элемента
     * @return HtmlNavElement или null
     */
    private HtmlNavElement findByIdCached(final Long id){
        updateCache();
        isUpdate.readLock().lock();
        try {
            return cacheList.stream().filter(x->x.getId().equals(id)).findFirst().orElse(null);
        } finally {
            isUpdate.readLock().unlock();
        }
    }

    /**
     * Обновление кэша
     */
    private void updateCache() {
        Date currentDate = new Date();
        if (needUpdate || lasUpdatingDate == null || currentDate.getTime()>lasUpdatingDate.getTime()+CACHE_UPDATING_PERIOD) {
            isUpdate.writeLock().lock();
            try {
                if (needUpdate || lasUpdatingDate == null || currentDate.getTime()>lasUpdatingDate.getTime()+CACHE_UPDATING_PERIOD) {
                    cacheList.clear();
                    cacheList.addAll(repository.findAll(Sort.by(Sort.Direction.ASC,"elementOrder")));

                    needUpdate = false;
                    lasUpdatingDate = currentDate;
                }

            } finally {
                isUpdate.writeLock().unlock();
            }
        }
    }

    /**
     * Получает HtmlNavElement элемент по name из кэша
     *
     * @param name name
     * @return HtmlNavElement or null
     */
    private HtmlNavElement findFirstByNameOrderByElementOrderCached(final String name) {
        updateCache();
        isUpdate.readLock().lock();
        try {
            return cacheList.stream().filter(x->x.getName().equals(name.toLowerCase())).findFirst().orElse(null);
        } finally {
            isUpdate.readLock().unlock();
        }
    }


    /**
     * Получает HtmlNavElement элемент по Id
     * @param id id
     * @return HtmlNavElement or null
     */
    public HtmlNavElement loadById(long id) {
        return findByIdCached(id);
    }

    /**
     * Получает HtmlNavElement элемент по name
     * @param name name
     * @return HtmlNavElement or null
     */
    public HtmlNavElement loadByName(String name) {
        if (name == null) throw new IllegalArgumentException("Null name in loadByName() is forbidden");
        return findFirstByNameOrderByElementOrderCached(name);
    }


    /**
     * Сохраняет элемент в БД
     * @param element HtmlNavElement
     */
    public void saveElement(HtmlNavElement element){
        if (element == null) throw new IllegalArgumentException("Null name in saveElement() is forbidden");
        isUpdate.writeLock().lock();
        try {
            needUpdate = true;
            repository.save(element);
        } finally {
            isUpdate.writeLock().unlock();
        }
    }

    /**
     * Возвращает список заголовков панели с учетом переданных прав доступа
     *
     * @param authorities коллекция прав доступа
     * @return List<HtmlNavElement>
     */
    @Override
    public List<HtmlNavElement> getNavHeaderElements(Collection<? extends GrantedAuthority> authorities) {
        List<HtmlNavElement> currentList = findByParentIsNullOrderByElementOrderCached();
        return currentList
                .stream()
                .map(x->x.getElementForUserAuthority(authorities))
                .filter(x -> x != null)
                .collect(Collectors.toList());
    }


    /**
     * Возвращает список заголовков панели как строку c массивом объектов в формате Json, с учетом прав доступа пользователя
     *
     * @return Json строка
     */
    @Override
    public String getNavHeaderElementsAsJson() throws JsonProcessingException {
        List<HtmlNavElement> currentList = findByParentIsNullOrderByElementOrderCached();
        ObjectMapper mapper = new ObjectMapper();
        String value = mapper.writeValueAsString(currentList);
        return value;
    }

    /**
     * Востанавливает Parent и сохраняет элементы в БД
     *
     * @param json строка в формате Json
     * @throws JsonProcessingException при ошибке парсинга
     */
    @Override
    public void saveNavHeaderElementFromJson(String json) throws JsonProcessingException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        HtmlNavElement element = mapper.readValue(json, HtmlNavElement.class);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(json);
        String parentIdString = jsonObject.get("parentId").toString();
        HtmlNavElement parent = null;
        if (!parentIdString.equals("#")) {
            parent = loadById(Long.valueOf(parentIdString));
        }
        element.setParent(parent);

        saveElement(element);
    }

    /**
     * Удаляет из базы данных элемент меню
     *
     * @param id удаляемого элемента
     */
    @Override
    public void deleteNavHeaderElement(Long id) {
        isUpdate.writeLock().lock();
        try {
            needUpdate = true;
            repository.deleteById(id);
        } finally {
            isUpdate.writeLock().unlock();
        }

    }

    @Autowired
    public void setRepository(HtmlNavElementsRepository repository) {
        this.repository = repository;
    }


}
