
package com.wip.dao;

import com.wip.model.OptionsDomain;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 */
@Mapper
public interface OptionDao {

    /**
     *
     * @return
     */
    List<OptionsDomain> getOptions();

    /**
     *
     * @param option
     */
    void updateOptionByName(OptionsDomain option);

    /**
     *
     * @param name
     * @return
     */
    OptionsDomain getOptionByName(String name);
}
