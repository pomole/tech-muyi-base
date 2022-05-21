package tech.muyi.common.constant.enumtype;

/**
 * description: ProfileActiveEnum
 * date: 2022/1/3 0:15
 * author: muyi
 * version: 1.0
 */
public enum ProfileActiveEnum {
    LOCAL("local","本地环境"),
    DEV("dev","开发环境"),
    PRE("pre","预发环境"),
    PROD("prod","生产环境");

    private String code;

    private String desc;

    private ProfileActiveEnum(String code,String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public static String getDesc(String code){
        for(ProfileActiveEnum item : ProfileActiveEnum.values()){
            if(item.getCode() == code){
                return item.desc;
            }
        }
        return "other";
    }
}
