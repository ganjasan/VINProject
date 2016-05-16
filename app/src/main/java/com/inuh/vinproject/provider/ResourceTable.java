package com.inuh.vinproject.provider;

import android.provider.BaseColumns;

/**
 * Created by artimus on 02.05.16.
 */
public interface ResourceTable extends BaseColumns {


    /*date of row updating
     <P>Type: NUMERIC (UNIX timastamp) </P>
     */
    public static final String _UPDATED = "updated";

    /*date of row creating
    <P>Type: NUNERIC (UNIX timestamp) </P>
     */
    public static final String _CREATED = "created";

    /* The current transaction status.  May be null if no current transaction for this resource
     * <P>Type: STRING</P>
     */
    public static final String _STATUS = "status";

    /* The transaction result code, typically an http status.
    * <P>Type: INTEGER </P>
    */
    public static final String _RESULT = "result";

    /* BACKENDLESS UID
    * <P>Type: STRING(500) </P>
    */
    public static final String _OBJECTID = "objectId";

    /* Active flag
    * <P> Type: BOOLEAN </P>
    */
    public static final String _ACTIVE = "active";



}
