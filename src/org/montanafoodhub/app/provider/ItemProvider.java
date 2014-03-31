package org.montanafoodhub.app.provider;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class ItemProvider extends ContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://org.montanafoodhub.provider.itemprovider/items");

    public static final String KEY_ID = "_id";
    public static final String KEY_ITEM_ID = "iid";
    public static final String KEY_PRODUCER_ID = "producer_id";
    public static final String KEY_IN_CSA = "in_csa";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_PRODUCT_DESC = "product_desc";
    public static final String KEY_PRODUCT_URL = "product_url";
    public static final String KEY_PRODUCT_IMAGE_URL = "product_image_url";
    public static final String KEY_UNITS_AVAILABLE = "units_available";
    public static final String KEY_UNIT_DESC = "unit_desc";
    public static final String KEY_UNIT_PRICE = "unit_price";
    public static final String KEY_DELIVERY_DATE = "delivery_date";
    public static final String KEY_NOTE = "note";

    private static final String Tag = "ItemProvider";

    private static final int ITEMS = 1;
    private static final int ITEM_ID = 2;

    private static final UriMatcher _uriMatcher;
    static {
        _uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        _uriMatcher.addURI("org.montanafoodhub.provider.itemprovider", "items", ITEMS);
        _uriMatcher.addURI("org.montanafoodhub.provider.itemprovider", "items/#", ITEM_ID);
    }

    private ItemProviderDBHelper _dbHelper;

    @Override
    public boolean onCreate() {

        // todo - figure out cursor factory
        _dbHelper = new ItemProviderDBHelper(getContext(), null);
        return true;
    }

    @Override
    public String getType(Uri uri) {
        String type = null;

        switch (_uriMatcher.match(uri)) {
            case ITEMS:
                type = "vnd.android.cursor.dir/vnd.org.montanafoodhub.provider.itemprovider";
                break;

            case ITEM_ID:
                type = "vnd.android.cursor.dir/vnd.org.montanafoodhub.provider.itemprovider";
                break;

            default:
                throw new IllegalArgumentException(Tag + " Unsupported URI: " + uri);
        }

        return type;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        sqlBuilder.setTables(ItemProviderDBHelper.ITEMS_TABLE);

        // if this item applies to a single item, add the where clause
        if (_uriMatcher.match(uri) == ITEM_ID) {
            String whereClause = String.format("%s = %d", KEY_ID, uri.getPathSegments().get(1));
            sqlBuilder.appendWhere(whereClause);
        }

        // default sort order is product
        String orderBy = sortOrder;
        if (TextUtils.isEmpty(orderBy)) {
            orderBy = KEY_CATEGORY;
        }

        // todo - should only need read only access
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        Cursor cursor = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, orderBy);

        // register the ContentResolver for notification if the cursor contents change
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Uri newUri = null;

        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        long insertId = db.insert(ItemProviderDBHelper.ITEMS_TABLE, null, values);
        if (insertId > 0) {
            newUri = ContentUris.withAppendedId(CONTENT_URI, insertId);
            getContext().getContentResolver().notifyChange(newUri, null);
        }

        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.w(Tag, "delete");

        String whereClause = null;

        switch (_uriMatcher.match(uri)) {
            case ITEMS:
                whereClause = selection;
                break;

            case ITEM_ID:
                String id = uri.getPathSegments().get(1);
                if (TextUtils.isEmpty(selection)) {
                    whereClause = String.format("%s = %s", KEY_ID, id);
                } else {
                    whereClause = String.format("%s = %s AND (%s)", KEY_ID, id, selection);
                }
                break;
        }

        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        int count = db.delete(ItemProviderDBHelper.ITEMS_TABLE, whereClause, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        String whereClause = null;

        switch (_uriMatcher.match(uri)) {
            case ITEMS:
                whereClause = selection;
                break;

            case ITEM_ID:
                String id = uri.getPathSegments().get(1);
                if (TextUtils.isEmpty(selection)) {
                    whereClause = String.format("%s = %s", KEY_ID, id);
                } else {
                    whereClause = String.format("%s = %s AND (%s)", KEY_ID, id, selection);
                }
                break;
        }

        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        int count = db.update(ItemProviderDBHelper.ITEMS_TABLE, values, whereClause, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }


    private static class ItemProviderDBHelper extends SQLiteOpenHelper {

        private static final String Tag = "ItemProviderDBHelper";

        // todo - move these out into thier own class that contains db level stuff
        private static final String DATABASE_NAME = "hub.db";
        private static final int DATABASE_VERSION = 1;
        private static final String ITEMS_TABLE = "items";

        public ItemProviderDBHelper (Context context, SQLiteDatabase.CursorFactory factory) {
            super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(Tag, "onCreate");

            String sqlCreate = String.format("create table %s "
                    + "(%s integer primary key autoincrement, "
                    + " %s TEXT, "
                    + " %s TEXT, "
                    + " %s INTEGER, "
                    + " %s TEXT, "
                    + " %s TEXT, "
                    + " %s TEXT, "
                    + " %s TEXT, "
                    + " %s INTEGER, "
                    + " %s TEXT, "
                    + " %s REAL);",
                    ITEMS_TABLE, KEY_ID, KEY_ITEM_ID, KEY_PRODUCER_ID, KEY_IN_CSA, KEY_CATEGORY, KEY_PRODUCT_DESC,
                    KEY_PRODUCT_URL, KEY_PRODUCT_IMAGE_URL, KEY_UNITS_AVAILABLE, KEY_UNIT_DESC, KEY_UNIT_PRICE);

            db.execSQL(sqlCreate);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.w(Tag, String.format("Upgrading database from version %d to version %d", oldVersion, newVersion));

            db.execSQL("DROP TABLE IF EXISTS " + ITEMS_TABLE);
            onCreate(db);
        }
    }

}