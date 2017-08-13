package com.example.milka.m0802.ContentResolver.Contact.Util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Milka on 2017/8/2.
 *
 * 通讯录工具类
 *
 *注意：
 * 为了确保您的数据安全请在使用工具前，对数据进行必要的合法性判断
 *
 * 本工具类依赖于./ContactBean.java文件中的ContactBean类，若无法获得文件，文件可从此java文件尾处获取
 *为避免造成多连接，使用了单例模式
 *
 * 函数使用说明：
 *
 * 1、获取工具实例，例如：ContactUtil myContactUtil = ContactUtil.getInstance(this);
 *
 * 2、调用对应的API
 *      2-1、增———
 *                      2-1-1、addContacts(ContactBean newBean):添加单人单条通信数据.
 *                      2-1-2、addContacts(ContactSuperBean contactSuperBean):添加单人多条通信数据
 *      2-2、删———deleteContactBean(ContactBean contactBean):删除特定contactBean,
 *      2-3、改———updateContact(ContactBean oldContactBean, ContactBean newContactBean)：修改特定的contactBean
 *      2-4、查———readContacts():遍历通讯录中的所有数据，并返回List<ContactBean>
 */

public class ContactUtil {

    private final String TAG = "ContactUtil";
    private Context context;

/**
 *
 * 配置信息，不可轻易修改
 * */
    private final String URI_CONTACT_TABLE_RAW_CONTACTS = "content://com.android.contacts/raw_contacts";
    private final String URI_CONTACT_TABLE_DATA = "content://com.android.contacts/data";
    private final String URI_CONTACT_TABLE_MIMETYPES = "content://com.android.contacts/mimetypes";
    private final String MIME_TYPE_ITEM_NAME = "vnd.android.cursor.item/name";
    private final String MIME_TYPE_ITEM_PHONE_V2 = "vnd.android.cursor.item/phone_v2";
    private final int CELL_PHONE_INDEX = 2;

    private static ContactUtil mInstance;
    private ContactUtil(Context context) {
        this.context = context;
    }
/**
 * 外部调用此函数获取实例
 * */
    public static ContactUtil getInstance(Context context){
        if (mInstance == null){
            synchronized (ContactUtil.class){
                if (null == mInstance){
                    mInstance = new ContactUtil(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 遍历通讯录，并返回list
     * */
    public List<ContactBean> readContacts(){
        List<ContactBean> data = new ArrayList<>();
        if (context == null) return data;
        Activity activity = (Activity)context;
        Cursor cursor = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst())
        do{
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number =  cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.i(TAG, name + number);
            data.add(new ContactBean(name, number));
        }while (cursor.moveToNext());
        if (cursor != null && !cursor.isClosed())cursor.close();
        return data;
    }
    /**
     * 添加联系人
     * */
    public void addContact(@Nullable ContactSuperBean contactSuperBean){
            /*插入raw_contacts表，并获取id*/
        Uri uri = Uri.parse(URI_CONTACT_TABLE_RAW_CONTACTS);
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        long contact_id = ContentUris.parseId(resolver.insert(uri, values));
        /*插入data表*/
        uri = Uri.parse(URI_CONTACT_TABLE_DATA);

        /*add name**********START*/
        values.put(ContactsContract.Data.RAW_CONTACT_ID, contact_id);
        values.put(ContactsContract.Data.MIMETYPE, MIME_TYPE_ITEM_NAME);
        values.put(ContactsContract.Data.DATA1, contactSuperBean.getName());
        /*ContactsContract.Data.DATA2处对应的该是名，后期可以优化修改*/
        values.put(ContactsContract.Data.DATA2, contactSuperBean.getName());
        resolver.insert(uri, values);
        values.clear();
       /*add name**********END*/

       /*add Phone*/
        for(String phoneNumber: contactSuperBean.getPhoneList()) {
            values.put(ContactsContract.Data.RAW_CONTACT_ID, contact_id);
            values.put(ContactsContract.Data.MIMETYPE, MIME_TYPE_ITEM_PHONE_V2);
            values.put(ContactsContract.Data.DATA1, phoneNumber);
            values.put(ContactsContract.Data.DATA2, CELL_PHONE_INDEX);
            resolver.insert(uri, values);
            values.clear();
        }

    }
    /**
     * 添加联系人
     * */
    public void addContact(@Nullable ContactBean newAddContact){

        /*插入raw_contacts表，并获取id*/
        Uri uri = Uri.parse(URI_CONTACT_TABLE_RAW_CONTACTS);
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        long contact_id = ContentUris.parseId(resolver.insert(uri, values));
        /*插入data表*/
        uri = Uri.parse(URI_CONTACT_TABLE_DATA);

        /*add name**********START*/
        values.put(ContactsContract.Data.RAW_CONTACT_ID, contact_id);
        values.put(ContactsContract.Data.MIMETYPE, MIME_TYPE_ITEM_NAME);
        values.put(ContactsContract.Data.DATA1, newAddContact.getPersonName());
        /*ContactsContract.Data.DATA2处对应的该是名，后期可以优化修改*/
        values.put(ContactsContract.Data.DATA2, newAddContact.getPersonName());
        resolver.insert(uri, values);
        values.clear();
       /*add name**********END*/

       /*add Phone*/
       values.put(ContactsContract.Data.RAW_CONTACT_ID, contact_id);
        values.put(ContactsContract.Data.MIMETYPE, MIME_TYPE_ITEM_PHONE_V2);
        values.put(ContactsContract.Data.DATA1, newAddContact.getPhoneNum());
        values.put(ContactsContract.Data.DATA2, CELL_PHONE_INDEX);
        resolver.insert(uri, values);
        values.clear();
    }
    /*
    * 改进删除数据
    * */
    public void deleteContactBean(ContactBean contactBean){
//        /*从Data表格中获取对应电话的raw_contact_id, 一个raw_contact_id代表一条用户数据*/
//        Uri uri = Uri.parse(URI_CONTACT_TABLE_DATA);
//        int targetContactId = -1;
//        Boolean isLastPhone = false;
//        ContentResolver resolver = context.getContentResolver();
//        String sql = ContactsContract.Data.DATA1 + " =?"+ " and " + ContactsContract.Data.DATA2 + " =? ";
//        String[] selectionArgs =  new String[]{phone, String.valueOf(CELL_PHONE_INDEX)};
//        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data.RAW_CONTACT_ID},sql, selectionArgs, null);
//        if (cursor != null && cursor.moveToFirst()){
//            do{
//                int raw_contact_id = cursor.getInt(0);
//                sql =
//                        ContactsContract.Data.MIMETYPE + " =?"
//                        + " and "
//                        + ContactsContract.Data.RAW_CONTACT_ID + " =?"
//                        + " and "
//                        + ContactsContract.Data.DATA1 + " =?";
//                String[] selectionArgsForName = new String[]{
//                        String.valueOf(MIME_TYPE_ITEM_NAME),
//                        String.valueOf(raw_contact_id),
//                        name
//                };
//                Cursor targetCursor = resolver.query(
//                        uri,
//                        new String[]{ContactsContract.Data.RAW_CONTACT_ID},
//                        sql,
//                        selectionArgsForName,
//                        null);
//                if (targetCursor != null && targetCursor.moveToFirst()){
//                   targetContactId = targetCursor.getInt(0);
//                    cursor.moveToLast();
//                }
//            }while (cursor.moveToNext());
//            if (targetContactId == -1) return;
            Boolean isLastPhone = false;
            int targetContactId = getRawContactId(contactBean.getPersonName(), contactBean.getPhoneNum());
            if ( targetContactId == -1) return;
            /*删除Data表中选中的对应的电话号码*/
            Uri uri = Uri.parse(URI_CONTACT_TABLE_DATA);
            ContentResolver resolver = context.getContentResolver();
            String sql = ContactsContract.Data.MIMETYPE + " =?"
                    +" and "+
                    ContactsContract.Data.RAW_CONTACT_ID + " =?";
            String[] selectionArgs =  new String[]{MIME_TYPE_ITEM_PHONE_V2, String.valueOf(targetContactId)};
            resolver.delete(uri, sql, selectionArgs);

            /*如果删除的号码是最后一条电话号码，则删除目标Raw_Contact的一条数据*/
            /*通过目标Raw_contact_id判断是否是最后一条phone记录是否存在，不存在则删除该用户所有信息*/
            sql = ContactsContract.Data.RAW_CONTACT_ID + " =?"
                    + " and "
                    + ContactsContract.Data.MIMETYPE + " =?";
            String[] selectionArgsForLast = new String[]{
                    String.valueOf(targetContactId),
                    String.valueOf(MIME_TYPE_ITEM_PHONE_V2)};
            Cursor cursorLastCheck = resolver.query(uri,new String[] {ContactsContract.Data._ID}, sql, selectionArgsForLast, null);
           // int i = cursorLastCheck.getCount();
            if (cursorLastCheck != null && cursorLastCheck.getCount() == 0) isLastPhone = true;
            cursorLastCheck.close();
            uri = Uri.parse(URI_CONTACT_TABLE_RAW_CONTACTS);
            String sqlForDelete = ContactsContract.RawContacts._ID + " =?";
            String[] selectionArgsForDelete = new String[]{String.valueOf(targetContactId)};
            if (isLastPhone) resolver.delete(uri, sqlForDelete, selectionArgsForDelete);

            /*最后一条数据删除Data中所有对应的RAW_CONTACT_ID的数据*/
            uri = Uri.parse(URI_CONTACT_TABLE_DATA);
            sql = ContactsContract.Data.RAW_CONTACT_ID + " =?";
            selectionArgs = new String[]{String.valueOf(targetContactId)};
            if(isLastPhone) resolver.delete(uri, sql, selectionArgs);
        }
    /**
     * 删除数据
     * 此函数存在BUG，请使用deleteContactBean(ContactBean contactBean)
     * @deprecated
     */
    public void deleteContact(String personName){
        Uri uri = Uri.parse(URI_CONTACT_TABLE_RAW_CONTACTS);
        ContentResolver resolver = context.getContentResolver();
        String sql = ContactsContract.Data.DISPLAY_NAME + "=?";;
        Cursor cursor = resolver.query(
                uri,
                new String[]{ContactsContract.Data._ID},
                sql,
                new String[]{personName}, null);
        if (cursor != null && cursor.moveToFirst()){
            int id = cursor.getInt(0);
            resolver.delete(uri, sql, new String[]{personName});
            uri = Uri.parse(URI_CONTACT_TABLE_DATA);
            String sql1 = ContactsContract.Data.DISPLAY_NAME + "=?";
            resolver.delete(uri, sql1, new String[]{String.valueOf(id)});
        }
        cursor.close();
    }
    /**
     * 改进更新数据
     * */
    public void updateContact(ContactBean oldContactBean, ContactBean newContactBean){
        int targetRawContactId = getRawContactId(oldContactBean.getPersonName(), oldContactBean.getPhoneNum());
        if ( targetRawContactId == -1) return;

        Uri uri = Uri.parse(URI_CONTACT_TABLE_DATA);
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(ContactsContract.Data.DATA1, newContactBean.getPhoneNum());
        String sqlUpdate =
                ContactsContract.Data.MIMETYPE + "=?"
                        +" and "+
                        ContactsContract.Data.RAW_CONTACT_ID + "=?";
        String[] selectionArgs =  new String[]{MIME_TYPE_ITEM_PHONE_V2, String.valueOf(targetRawContactId)};
        //Log.i(TAG, String.valueOf(id));
        resolver.update(uri, values, sqlUpdate, selectionArgs);
            /*更新姓名*/
        values.clear();
        values.put(ContactsContract.Data.DATA1, newContactBean.getPersonName());
        values.put(ContactsContract.Data.DATA2, newContactBean.getPersonName());
        sqlUpdate =
                ContactsContract.Data.MIMETYPE + "=?"
                        +" and "+
                        ContactsContract.Data.RAW_CONTACT_ID + "=?";
        selectionArgs =  new String[]{MIME_TYPE_ITEM_NAME, String.valueOf(targetRawContactId)};
        resolver.update(uri, values, sqlUpdate, selectionArgs);

    }

    /**
     * 更新数据
     * @param oldContactBean ContactBean object which will be change
     * @param newBean ContactBean object which will replace oldContactBean
     * @deprecated
     *
     * */
    public void editContact(ContactBean oldContactBean, ContactBean newBean) {
        if (oldContactBean == null || newBean == null) return;

        String oldName = oldContactBean.getPersonName();
        String oldPhone = oldContactBean.getPhoneNum();
        String newName = newBean.getPersonName();
        String newPhone = newBean.getPhoneNum();

        /*获取用户的id， 通过id改变data表格中的相关数据*/
        Uri uri = Uri.parse(URI_CONTACT_TABLE_RAW_CONTACTS);
        ContentResolver resolver = context.getContentResolver();
        String sqlForId = ContactsContract.Data.DISPLAY_NAME + "=?";
        Cursor cursorForId = resolver.query(
                uri,
                new String[]{ContactsContract.Data._ID},
                sqlForId,
                new String[]{oldName},
                null
        );
        if (cursorForId != null && cursorForId.moveToFirst())
        do {
            /*更新电话号码*/
            int id = cursorForId.getInt(0);
            uri = Uri.parse(URI_CONTACT_TABLE_DATA);
            ContentValues values = new ContentValues();

            values.put(ContactsContract.Data.DATA1, newPhone);
            String sqlUpdate =
                    ContactsContract.Data.MIMETYPE + "=?"
                    +" and "+
                    ContactsContract.Data.RAW_CONTACT_ID + "=?";
            String[] selectionArgs =  new String[]{MIME_TYPE_ITEM_PHONE_V2, String.valueOf(id)};
            //Log.i(TAG, String.valueOf(id));
            resolver.update(uri, values, sqlUpdate, selectionArgs);
            /*更新姓名*/
            values.clear();
            values.put(ContactsContract.Data.DATA1, newName);
            values.put(ContactsContract.Data.DATA2, newName);
            sqlUpdate =
                    ContactsContract.Data.MIMETYPE + "=?"
                            +" and "+
                            ContactsContract.Data.RAW_CONTACT_ID + "=?";
           selectionArgs =  new String[]{MIME_TYPE_ITEM_NAME, String.valueOf(id)};
            resolver.update(uri, values, sqlUpdate, selectionArgs);
        }while (cursorForId.moveToNext());
        cursorForId.close();
    }
    /**
     * 通过通讯人的名字和姓名获取唯一的RAW_CONTACT_ID
     * @param name 通信对象的名字
     * @param phone 通讯对象的电话号码：vnd.android.cursor.item/phone_v2
     * */

    private int getRawContactId(String name, String phone) {
        /*从Data表格中获取对应电话的raw_contact_id, 一个raw_contact_id代表一条用户数据*/
        Uri uri = Uri.parse(URI_CONTACT_TABLE_DATA);
        int targetContactId = -1;
        ContentResolver resolver = context.getContentResolver();
        String sql = ContactsContract.Data.DATA1 + " =?" + " and " + ContactsContract.Data.DATA2 + " =? ";
        String[] selectionArgs = new String[]{phone, String.valueOf(CELL_PHONE_INDEX)};
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data.RAW_CONTACT_ID}, sql, selectionArgs, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int raw_contact_id = cursor.getInt(0);
                sql =
                        ContactsContract.Data.MIMETYPE + " =?"
                                + " and "
                                + ContactsContract.Data.RAW_CONTACT_ID + " =?"
                                + " and "
                                + ContactsContract.Data.DATA1 + " =?";
                String[] selectionArgsForName = new String[]{
                        String.valueOf(MIME_TYPE_ITEM_NAME),
                        String.valueOf(raw_contact_id),
                        name
                };
                Cursor targetCursor = resolver.query(
                        uri,
                        new String[]{ContactsContract.Data.RAW_CONTACT_ID},
                        sql,
                        selectionArgsForName,
                        null);
                if (targetCursor != null && targetCursor.moveToFirst()) {
                    targetContactId = targetCursor.getInt(0);
                    cursor.moveToLast();
                }
                targetCursor.close();
            } while (cursor.moveToNext());
            cursor.close();
        }
        return targetContactId;
    }
}
/***********************************************************************/
//  文件名 : ContactBean.java
/***********************************************************************/
// public class ContactBean {
//
//    private String personName;
//    private String phoneNum;
//    /**
//     * @param personName 联系人姓名
//     * @param phoneNum 联系人电话号码
//     */
//    public ContactBean(@Nullable String personName, @Nullable String phoneNum) {
//        this.personName = personName;
//        this.phoneNum = phoneNum;
//    }
//
//    public String getPersonName() {
//        return personName;
//    }
//
//    public void setPersonName(String personName) {
//        this.personName = personName;
//    }
//
//    public String getPhoneNum() {
//        return phoneNum;
//    }
//
//    public void setPhoneNum(String phoneNum) {
//        this.phoneNum = phoneNum;
//    }
//}