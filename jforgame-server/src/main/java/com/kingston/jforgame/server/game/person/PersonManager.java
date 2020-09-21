package com.kingston.jforgame.server.game.person;

import com.kingston.jforgame.server.doctor.Person;
import com.kingston.jforgame.server.game.person.message.req.ReqPersonToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author puMengBin
 * @Date 2020-09-07 19:12
 * @Description
 */
public class PersonManager {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void personToString(ReqPersonToString reqPersonToString) {
        logger.error( "{}  {}" ,reqPersonToString.getName(), new Person().toString());
    }

}
