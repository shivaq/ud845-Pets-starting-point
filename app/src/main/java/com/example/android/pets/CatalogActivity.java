/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.pets.data.PetDbHelper;
import com.example.android.pets.data.PetContract.PetEntry;


/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

    //DB helper that will provide us access to the DB
    private PetDbHelper mDbHelper;//メンバーとして作成 →onCreate でインスタンス化

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        //To access our database, we instantiate our subclass of SQLiteOpenHelper
        //and pass the context, which is the current activity
        mDbHelper = new PetDbHelper(this);//onCreate内で、DBヘルパーはインスタンス化するんだね

    }

    //To display DB info when user back from other activity
    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();//DB が生成/Open される .open shelter.db をプロンプトで行うのと同じことしてる

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        Cursor cursor = db.rawQuery("SELECT * FROM " + PetEntry.TABLE_NAME, null);//ペットテーブルのすべての行を含む cursor を取得
        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.text_view_pet);
            displayView.setText("Number of rows in pets database table: " + cursor.getCount());//cursorの getcount
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();//読み込み完了したら、cursorを閉じること！ →リソース解放 + 無効にするという効用あり
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }
    private void insertPet(){

        //Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();//データレポジトリ取得。書き込みモード

        //Define dummy values
        String dummyName = "Toto";
        String dummyBreed = "Terrier";
        int dummyGender = PetEntry.GENDER_MALE;
        int dummyWeight = 7;

        //Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, dummyName);
        values.put(PetEntry.COLUMN_PET_BREED, dummyBreed);
        values.put(PetEntry.COLUMN_PET_GENDER, dummyGender);
        values.put(PetEntry.COLUMN_PET_WEIGHT, dummyWeight);

        //Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(PetEntry.TABLE_NAME, null, values);//このプライマリーキわず

        Log.v("CatalogActivity", "New row ID " + newRowId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
