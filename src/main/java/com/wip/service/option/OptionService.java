
package com.wip.service.option;

import com.wip.model.OptionsDomain;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface OptionService {

    /**
     *
     * @return
     */
    List<OptionsDomain> getOptions();

    /**
     *
     * @param querys
     */
    void saveOptions(Map<String,String> querys);

    /**
     *
     * @param s
     * @param s1
     */
    void updateOptionByName(String name, String value);

    /**
     *
     * @param site_record
     * @return
     */
    OptionsDomain getOptionByName(String site_record);
}
