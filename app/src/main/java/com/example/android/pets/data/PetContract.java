package com.example.android.pets.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Yasuaki on 2016/09/26.
 */

public final class PetContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private PetContract() {}//空のコンストラクタにはわけがある

    //define constants for content providers URI
    static final String CONTENT_AUTHORITY = "com.example.android.pets";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    static final String PATH_PETS = "pets";

    public static final class PetEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);

        static final String TABLE_NAME = "pets";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PET_NAME = "name";
        public static final String COLUMN_PET_BREED = "breed";
        public static final String COLUMN_PET_GENDER = "gender";
        public static final String COLUMN_PET_WEIGHT = "weight";

        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
        public static final int GENDER_UNKNOWN = 0;

        public static boolean isValidWeight(String weightString){
            return !weightString.equals("");
        }

        /**
         * Returns whether or not the given gender is GENDER_* constant.
         */
        static boolean isValidGender(int gender){
/*            if(gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE){
                return true;
            }
            return false;*/
            return gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE;
        }
    }

}
