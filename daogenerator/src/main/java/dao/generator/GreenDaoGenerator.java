package dao.generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Author:Jenny
 * Date:16/6/7 14:32
 * E-mail:fishloveqin@gmail.com
 * ORM 数据表生成器
 **/
public class GreenDaoGenerator {
    private static final int VERSION = 1;

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(VERSION, "com.tgf.kcwc");
        schema.setDefaultJavaPackageDao("com.tgf.kcwc.db.dao");
        addUserInfo(schema);
        new DaoGenerator().generateAll(schema, "app/src/main/java-gen");

    }

    /**
     * 创建用户基本信息表
     * @param schema
     */
    private static void addUserInfo(Schema schema) {
        Entity userInfo = schema.addEntity("UserInfoBean");
        userInfo.addIdProperty();
        userInfo.addLongProperty("userId");
        userInfo.addStringProperty("userName");
        userInfo.addStringProperty("niceName");
        userInfo.addStringProperty("tel");
        userInfo.addStringProperty("token");
        userInfo.addStringProperty("avatar");//头像
        userInfo.addStringProperty("gender");
    }

}
