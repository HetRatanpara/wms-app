package com.example.wms.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wms.data.DBHelper;
import com.example.wms.R;
import com.example.wms.models.Supplier;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class SupplierViewAdapter extends RecyclerView.Adapter<SupplierViewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Supplier> supplierList;
    private DBHelper dbHelper;

    private TextInputLayout edt_supplier_name,edt_supplier_code,edt_supplier_email,edt_supplier_phone;
    private ImageButton fullscreen_dialog_save,fullscreen_dialog_close;
    private TextView add_supplier_dialog;


    public SupplierViewAdapter(Context mContext,ArrayList<Supplier> supplierList) {
        this.mContext = mContext;
        this.supplierList = supplierList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.supplier_list_row,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    public void updateDataSet(ArrayList<Supplier>supplierList) {
        this.supplierList = supplierList;
        notifyDataSetChanged();
    }

    public void filterList(ArrayList<Supplier> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        supplierList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }




    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();
        holder.txt_supplier_name.setText(supplierList.get(position).getSupplier_name());
        holder.txt_supplier_code.setText(supplierList.get(position).getCode());
        holder.txt_supplier_email.setText(supplierList.get(position).getEmail());
        holder.txt_supplier_phone.setText(supplierList.get(position).getPhone());

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(mContext,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.add_supplier_dialog);


                add_supplier_dialog = dialog.findViewById(R.id.add_supplier_dialog);
                edt_supplier_name = dialog.findViewById(R.id.edt_supplier_name);
                edt_supplier_code = dialog.findViewById(R.id.edt_supplier_code);
                edt_supplier_email = dialog.findViewById(R.id.edt_supplier_email);
                edt_supplier_phone = dialog.findViewById(R.id.edt_supplier_phone);
                fullscreen_dialog_close = dialog.findViewById(R.id.fullscreen_dialog_close);
                fullscreen_dialog_save = dialog.findViewById(R.id.fullscreen_dialog_save);

                Integer s_id = supplierList.get(position).getId();
                String s_name = supplierList.get(position).getSupplier_name();
                String s_code = supplierList.get(position).getCode();
                String s_phone = supplierList.get(position).getPhone();
                String s_email = supplierList.get(position).getEmail();


                edt_supplier_name.setEnabled(false);
                edt_supplier_code.setEnabled(false);
                edt_supplier_email.setEnabled(false);
                edt_supplier_phone.setEnabled(false);
                fullscreen_dialog_save.setVisibility(View.GONE);

                add_supplier_dialog.setText(s_name);
                edt_supplier_name.getEditText().setText(s_name);
                edt_supplier_code.getEditText().setText(s_code);
                edt_supplier_email.getEditText().setText(s_email);
                edt_supplier_phone.getEditText().setText(s_phone);

                fullscreen_dialog_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        holder.btn_supplier_list_item_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog = new Dialog(mContext,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.add_supplier_dialog);


                add_supplier_dialog = dialog.findViewById(R.id.add_supplier_dialog);
                edt_supplier_name = dialog.findViewById(R.id.edt_supplier_name);
                edt_supplier_code = dialog.findViewById(R.id.edt_supplier_code);
                edt_supplier_email = dialog.findViewById(R.id.edt_supplier_email);
                edt_supplier_phone = dialog.findViewById(R.id.edt_supplier_phone);
                fullscreen_dialog_close = dialog.findViewById(R.id.fullscreen_dialog_close);
                fullscreen_dialog_save = dialog.findViewById(R.id.fullscreen_dialog_save);

                Integer s_id = supplierList.get(position).getId();
                String s_name = supplierList.get(position).getSupplier_name();
                String s_code = supplierList.get(position).getCode();
                String s_phone = supplierList.get(position).getPhone();
                String s_email = supplierList.get(position).getEmail();


                add_supplier_dialog.setText(s_name);
                edt_supplier_name.getEditText().setText(s_name);
                edt_supplier_code.getEditText().setText(s_code);
                edt_supplier_email.getEditText().setText(s_email);
                edt_supplier_phone.getEditText().setText(s_phone);


                fullscreen_dialog_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (validateData()){
                            String edt_s_name = edt_supplier_name.getEditText().getText().toString();
                            String edt_s_code = edt_supplier_code.getEditText().getText().toString();
                            String edt_s_email = edt_supplier_email.getEditText().getText().toString();
                            String edt_s_phone = edt_supplier_phone.getEditText().getText().toString();
                            String edt_s_id = String.valueOf(supplierList.get(position).getId());

                            boolean resupdate = dbHelper.update_supplier_data(edt_s_id,edt_s_name,edt_s_code,edt_s_phone,edt_s_email);

                            if (resupdate == true){
                                Toast.makeText(mContext, "Supplier Update Successfully!", Toast.LENGTH_SHORT).show();
                                supplierList.set(position,new Supplier(supplierList.get(position).getId(),edt_s_name,edt_s_code,edt_s_phone,edt_s_email));
                                notifyItemChanged(position);
                                notifyDataSetChanged();
                            }else{
                                Toast.makeText(mContext, "Supplier could not be update", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    }
                });


                fullscreen_dialog_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        holder.btn_supplier_list_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String supplier_id = String.valueOf(supplierList.get(position).getId());
                String supplier_name = supplierList.get(position).getSupplier_name();

                AlertDialog.Builder dialogDelete= new AlertDialog.Builder(mContext);
                dialogDelete.setTitle("Warning");
                dialogDelete.setMessage(Html.fromHtml("Are you sure you want to delete <b>"+ supplier_name+"</b>?"));

                dialogDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int ans=  dbHelper.delete_supplier_Data(supplier_id);
                        if (ans>0){
                            Toast.makeText(mContext, "Supplier is Deleted", Toast.LENGTH_SHORT).show();
                            supplierList.remove(position);
                            notifyItemRemoved(position);
                        }else {
                            Toast.makeText(mContext, "Supplier is not deleted", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dialogDelete.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                dialogDelete.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return supplierList.size();
    }

    public class MyViewHolder  extends  RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView txt_supplier_name,txt_supplier_code,txt_supplier_phone,txt_supplier_email;
        private ConstraintLayout constraintLayout;
        private Button btn_supplier_list_item_delete,btn_supplier_list_item_edit;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            dbHelper = new DBHelper(mContext);
            txt_supplier_name = itemView.findViewById(R.id.text_supplier_name);
            txt_supplier_code = itemView.findViewById(R.id.text_supplier_code);
            txt_supplier_email = itemView.findViewById(R.id.text_supplier_email);
            txt_supplier_phone = itemView.findViewById(R.id.text_supplier_phone);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            btn_supplier_list_item_delete = itemView.findViewById(R.id.btn_supplier_list_item_delete);
            btn_supplier_list_item_edit = itemView.findViewById(R.id.btn_supplier_list_item_edit);

        }

        @Override
        public void onClick(View view) {

        }
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

}
