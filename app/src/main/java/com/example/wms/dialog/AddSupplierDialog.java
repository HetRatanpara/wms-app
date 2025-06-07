package com.example.wms.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.wms.R;
import com.example.wms.data.DBHelper;
import com.example.wms.fragments.SupplierFragment;
import com.google.android.material.textfield.TextInputLayout;

public class AddSupplierDialog extends DialogFragment implements View.OnClickListener {


    View view;
    TextInputLayout edt_supplier_name, edt_supplier_code, edt_supplier_email, edt_supplier_phone;
    ImageButton fullscreen_dialog_save, fullscreen_dialog_close;
    DBHelper dbHelper;
    boolean isDataEntered = false;
    private static SupplierFragment.SupplierDataCallback supplierDataCallback;

    public static AddSupplierDialog newInstance(SupplierFragment.SupplierDataCallback supplierDataCallback) {
        AddSupplierDialog.supplierDataCallback = supplierDataCallback;
        return new AddSupplierDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_supplier_dialog, container, false);

        dbHelper = new DBHelper(getContext());

        edt_supplier_name = view.findViewById(R.id.edt_supplier_name);
        edt_supplier_code = view.findViewById(R.id.edt_supplier_code);
        edt_supplier_email = view.findViewById(R.id.edt_supplier_email);
        edt_supplier_phone = view.findViewById(R.id.edt_supplier_phone);
        fullscreen_dialog_close = view.findViewById(R.id.fullscreen_dialog_close);
        fullscreen_dialog_save = view.findViewById(R.id.fullscreen_dialog_save);


        fullscreen_dialog_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateData()) {
                    String edt_s_name = edt_supplier_name.getEditText().getText().toString();
                    String edt_s_code = edt_supplier_code.getEditText().getText().toString();
                    String edt_s_email = edt_supplier_email.getEditText().getText().toString();
                    String edt_s_phone = edt_supplier_phone.getEditText().getText().toString();
                    boolean result = dbHelper.insert_supplier(edt_s_name, edt_s_code, edt_s_phone, edt_s_email);
                    if (result) {
                        Toast.makeText(getContext(), "Supplier added successfully!", Toast.LENGTH_SHORT).show();
                        supplierDataCallback.isInserted(true);
                        dismiss();
                    } else {
                        supplierDataCallback.isInserted(false);
                        Toast.makeText(getContext(), "Supplier could not be added", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        fullscreen_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        return view;
    }

    public boolean validateData() {
        boolean isDataValid = true;
        edt_supplier_name.setError(null);
        edt_supplier_code.setError(null);
        edt_supplier_email.setError(null);
        edt_supplier_phone.setError(null);
        if (TextUtils.isEmpty(edt_supplier_name.getEditText().getText().toString())) {
            isDataValid = false;
            edt_supplier_name.setError("Required");
        } else if (TextUtils.isEmpty(edt_supplier_code.getEditText().getText().toString())) {
            isDataValid = false;
            edt_supplier_code.setError("Required");
        } else if (TextUtils.isEmpty(edt_supplier_email.getEditText().getText().toString())) {
            isDataValid = false;
            edt_supplier_email.setError("Required");
        } else if (TextUtils.isEmpty(edt_supplier_phone.getEditText().getText().toString())) {
            isDataValid = false;
            edt_supplier_phone.setError("Required");
        }
        return isDataValid;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {

            case R.id.fullscreen_dialog_close:
                dismiss();
                break;

        }
    }
}
