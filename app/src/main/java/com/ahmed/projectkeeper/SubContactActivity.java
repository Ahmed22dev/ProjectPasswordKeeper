package com.ahmed.projectkeeper;

import android.app.AlertDialog;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmed.sqlite.helper.DatabaseHelper;
import com.ahmed.sqlite.model.SubContactModel;

public class SubContactActivity extends AppCompatActivity {

    private EditText edtxtName, edtxtUsrName, edtxtUsrPass,edtxtWebsite,edtxtNote;
    private KeyListener mKeyListener1,mKeyListener2,mKeyListener3,mKeyListener4,mKeyListener5;
    private TextView txtCreated;
    private String cName,cUsrName,cPass,cWebsite,cNote;
    private SubContactModel subContactModel;
    private DatabaseHelper db;
    private long parentId;
    private boolean fromEmail , onEditPressed= false;
    private long row_id;

    //for encryption and decryption
    private String seedValue = "I don't know what is this";
    private String normalTextEnc;
    private String normalTextDec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mail_or_contact);

        edtxtName = (EditText)findViewById(R.id.contactName);
        edtxtUsrName = (EditText)findViewById(R.id.userName);
        edtxtUsrPass = (EditText)findViewById(R.id.contactPassword);
        edtxtWebsite = (EditText)findViewById(R.id.contactwebsite);
        edtxtNote = (EditText)findViewById(R.id.contactnotes);
        txtCreated = (TextView)findViewById(R.id.supCreated);


        row_id = getIntent().getLongExtra("long",1L);
        fromEmail = getIntent().getBooleanExtra("boolean",false);


        db = new DatabaseHelper(getApplicationContext());

        if (fromEmail){

            disableEditText();

            db = new DatabaseHelper(getApplicationContext());
            db.getOneSubContact(row_id);
            edtxtName.setText(db.getOneSubContact(row_id).getS_name());
            edtxtUsrName.setText(db.getOneSubContact(row_id).getS_user_name());
            try {
                normalTextDec = AESHelper.decrypt(seedValue,db.getOneSubContact(row_id).getS_password());
            } catch (Exception e) {
                e.printStackTrace();
            }

            edtxtUsrPass.setText(normalTextDec);
            edtxtWebsite.setText(db.getOneSubContact(row_id).getS_website());
            edtxtNote.setText(db.getOneSubContact(row_id).getS_note());

            txtCreated.setVisibility(View.VISIBLE);
            txtCreated.setText("Last Updated "+db.getOneSubContact(row_id).getCreated_at());

            onCopy();

        }
    }

    public void onCopy(){
        edtxtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyText(edtxtName);
                Toast.makeText(SubContactActivity.this, "Text Copied", Toast.LENGTH_SHORT).show();
            }
        });
        edtxtUsrName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyText(edtxtUsrName);
                Toast.makeText(SubContactActivity.this, "Text Copied", Toast.LENGTH_SHORT).show();
            }
        });
        edtxtUsrPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyText(edtxtUsrPass);
                Toast.makeText(SubContactActivity.this, "Text Copied", Toast.LENGTH_SHORT).show();
            }
        });
        edtxtWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyText(edtxtWebsite);
                Toast.makeText(SubContactActivity.this, "Text Copied", Toast.LENGTH_SHORT).show();
            }
        });
        edtxtNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyText(edtxtNote);
                Toast.makeText(SubContactActivity.this, "Text Copied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void whenDone(){

            parentId = getIntent().getLongExtra("long",1L);
            cName = edtxtName.getText().toString();
            cUsrName = edtxtUsrName.getText().toString();
            cPass = edtxtUsrPass.getText().toString();
            cWebsite = edtxtWebsite.getText().toString();
            cNote = edtxtNote.getText().toString();

            db = new DatabaseHelper(getApplicationContext());
            subContactModel = new SubContactModel();

            subContactModel.setParentId(parentId);
            subContactModel.setS_name(cName);
            subContactModel.setS_user_name(cUsrName);
        try {
            normalTextEnc = AESHelper.encrypt(seedValue, cPass);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            subContactModel.setS_password(normalTextEnc);
            subContactModel.setS_website(cWebsite);
            subContactModel.setS_note(cNote);

            db.createSubContact(subContactModel);

            finish();
    }
    public void onDelete(){


        new AlertDialog.Builder(SubContactActivity.this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        db = new DatabaseHelper(getApplicationContext());
                        row_id = getIntent().getLongExtra("long",1L);
                        db.deleteSubContact(row_id);

                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
//                .setIcon(android.R.drawable.ic_dialog_info)
                .show();

    }
    public void onUpdate(){
        enableEditText();
        onEditPressed = true;

    }
    public void onUpdateDone(){

        row_id = getIntent().getLongExtra("long",1L);

            db = new DatabaseHelper(getApplicationContext());
            subContactModel = new SubContactModel();

            cName = edtxtName.getText().toString();
            cUsrName = edtxtUsrName.getText().toString();
            cPass = edtxtUsrPass.getText().toString();
            cWebsite = edtxtWebsite.getText().toString();
            cNote = edtxtNote.getText().toString();

            subContactModel.setS_row_id(row_id);
            subContactModel.setS_name(cName);
            subContactModel.setS_user_name(cUsrName);
            try {
                normalTextEnc = AESHelper.encrypt(seedValue, cPass);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            subContactModel.setS_password(normalTextEnc);
            subContactModel.setS_website(cWebsite);
            subContactModel.setS_note(cNote);

            db.updateSubContact(subContactModel);
            disableEditText();
            onEditPressed = false;

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtxtName.getWindowToken(), 0);
    }
    public void onCancel(){
        disableEditText();
        onEditPressed = false;
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtxtName.getWindowToken(), 0);
    }

    public void copyText(EditText tdtxt) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.setText(tdtxt.getText().toString());
    }

    public void disableEditText(){
//        edtxtName.setTextColor(Color.parseColor("#FFFFFF"));
        mKeyListener1 = edtxtName.getKeyListener();
        edtxtName.setKeyListener(null);

//        edtxtUsrName.setTextColor(Color.parseColor("#FFFFFF"));
        mKeyListener2 = edtxtUsrName.getKeyListener();
        edtxtUsrName.setKeyListener(null);

//        edtxtUsrPass.setTextColor(Color.parseColor("#FFFFFF"));
        mKeyListener3 = edtxtUsrPass.getKeyListener();
        edtxtUsrPass.setKeyListener(null);

        mKeyListener4 = edtxtWebsite.getKeyListener();
        edtxtWebsite.setKeyListener(null);

        mKeyListener5 = edtxtNote.getKeyListener();
        edtxtNote.setKeyListener(null);
    }
    public void enableEditText(){
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edtxtName, 0);

        edtxtName.setSelection(edtxtName.getText().length());

        edtxtName.setTextColor(Color.parseColor("#000000"));
        edtxtUsrName.setTextColor(Color.parseColor("#000000"));
        edtxtUsrPass.setTextColor(Color.parseColor("#000000"));
        edtxtWebsite.setTextColor(Color.parseColor("#000000"));
        edtxtNote.setTextColor(Color.parseColor("#000000"));

        edtxtName.setKeyListener(mKeyListener1);
        edtxtUsrName.setKeyListener(mKeyListener2);
        edtxtUsrPass.setKeyListener(mKeyListener3);
        edtxtWebsite.setKeyListener(mKeyListener4);
        edtxtNote.setKeyListener(mKeyListener5);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        fromEmail = getIntent().getBooleanExtra("boolean",false);

        if (onEditPressed){
            inflater.inflate(R.menu.actionbar_updone_btn, menu);
        } else if (fromEmail) {
            inflater.inflate(R.menu.actionbar_edt_delt, menu);
        } else {
            inflater.inflate(R.menu.actionbar_done_btn, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.action_done:
                whenDone();
                return true;
            case R.id.action_delete:
                onDelete();
                return true;
            case R.id.action_edit:
                invalidateOptionsMenu();
                onUpdate();
                return true;
            case R.id.action_update_done:
                invalidateOptionsMenu();
                onUpdateDone();
                return true;
            case R.id.action_update_cancel:
                invalidateOptionsMenu();
                onCancel();
                return true;
            // work around to handel on resume on EmailMainActivity.class
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem editDone = menu.findItem(R.id.action_update_done);

        if (onEditPressed) {
            editDone.setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    protected void onPause() {
        super.onPause();
        SecurityModerator.lockAppStoreTime();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SecurityModerator.lockAppCheck(this);
    }
}
