package com.inuh.vinproject.provider;

import java.sql.SQLException;
import java.util.List;

import com.inuh.vinproject.model.LoadNovel;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;


public class LoadNovelDAO extends BaseDaoImpl<LoadNovel, Integer> {

    protected LoadNovelDAO(ConnectionSource connectionSource,
                           Class<LoadNovel> dataClass) throws SQLException{
        super(connectionSource, dataClass);
    }

    public List<LoadNovel> getAllLoadNovels() throws SQLException{
        return this.queryForAll();
    }


}
