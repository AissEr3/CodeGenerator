package ${basePackage}.model;

import lombok.Data;

/**
 * @author ${author}
 * @created by ${author}
 */
@Data
public class DataModel {
<#list modelConfig.models as modelInfo>
    /**
    * ${modelInfo.description}
    */
    private ${modelInfo.type} ${modelInfo.fieldName}<#if modelInfo.defaultValue??> = ${modelInfo.defaultValue?c}</#if>;

</#list>
}
