
package com.wip.dao;

import com.wip.model.RelationShipDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
@Mapper
public interface RelationShipDao {

    /**
     *
     * @param mid
     * @return
     */
    List<RelationShipDomain> getRelationShipByMid(Integer mid);

    /**
     *
     * @param mid
     */
    void deleteRelationShipByMid(Integer mid);

    /**
     *
     * @param cid
     * @param mid
     * @return
     */
    Long getCountById(@Param("cid") Integer cid, @Param("mid") Integer mid);

    /**
     *
     * @param relationShip
     * @return
     */
    int addRelationShip(RelationShipDomain relationShip);

    /**
     *
     * @param cid
     */
    void deleteRelationShipByCid(int cid);

    /**
     *
     * @param cid
     */
    List<RelationShipDomain> getRelationShipByCid(Integer cid);
}
