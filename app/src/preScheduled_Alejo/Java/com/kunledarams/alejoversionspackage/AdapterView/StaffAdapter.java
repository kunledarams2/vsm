package com.kunledarams.alejoversionspackage.Utili;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.kunledarams.alejoversionspackage.Model.RegistrationForm;
import com.kunledarams.alejoversionspackage.R;
import com.kunledarams.alejoversionspackage.Staff_Infro;

import java.util.ArrayList;

/**
 * Created by ok on 9/3/2018.
 */

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.Mholder> {

    private Activity activity;
    private static ArrayList<RegistrationForm>forms;


    public StaffAdapter(Activity activity, ArrayList<RegistrationForm>forms) {
        this.activity = activity;
        this.forms=forms;
    }

    public StaffAdapter (ArrayList<RegistrationForm>osBundle){
        forms=osBundle;
    }

    public class Mholder extends RecyclerView.ViewHolder{

        TextView staffName, staffPhone, staffDepartment;
        LinearLayout layout;
        RegistrationForm form;
        View getView;
        String firstLetter;
        ImageView imageView;
        ArrayList<String>staffInfro=new ArrayList<>();

        public Mholder(final View itemView) {
            super(itemView);


            staffName= (TextView)itemView.findViewById(R.id.staffName);
            staffPhone=(TextView)itemView.findViewById(R.id.staffPhone);
            layout=(LinearLayout)itemView.findViewById(R.id.staff_id);
            imageView=(ImageView)itemView.findViewById(R.id.imageInitials);
            staffDepartment=(TextView)itemView.findViewById(R.id.staffDepartment);


            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    staffInfro.add(form.getEmployeeName());//0
                    staffInfro.add(form.getEmployeeDepartment());
                    staffInfro.add(form.getEmployeePhone());
                    staffInfro.add(form.getEmployeeEmail());



                      Intent intent= new Intent(itemView.getContext(), Staff_Infro.class);
                    intent.putStringArrayListExtra("staffInfro",(ArrayList<String>)staffInfro);
                    Toast.makeText(itemView.getContext(),staffInfro.get(0),Toast.LENGTH_LONG).show();
                    itemView.getContext().startActivity(intent);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        Explode explode= new Explode();
                        activity.overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                    }



                   /*   AlertDialog.Builder builder= new AlertDialog.Builder(view.getContext());
                     builder.setTitle("Staff Infro");
                    final View view1= activity.getLayoutInflater().inflate(R.layout.activity_staff__infro,null);

                    final TextView staffName= (TextView)view1.findViewById(R.id.staffName);
                    final TextView staffdepartment=(TextView)view1.findViewById(R.id.staffDepartment);
                    final TextView staffPhone = (TextView)view1.findViewById(R.id.staffPhone);
                    final TextView staffEmail = (TextView)view1.findViewById(R.id.staffEmail);
                    imageView=(ImageView)view1.findViewById(R.id.imageStaff);


                    // setting the staff content

                    staffName.setText(form.getEmployeeName());

                    ColorGenerator generator= ColorGenerator.MATERIAL;
                    String firstLetter= String.valueOf(form.getEmployeeName().charAt(0));
                    TextDrawable drawable=TextDrawable.builder().buildRound(firstLetter,generator.getRandomColor());
                    imageView.setImageDrawable(drawable);

                    staffdepartment.setText(form.getEmployeeDepartment());
                    staffPhone.setText(form.getEmployeePhone());
                    staffPhone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + form.getEmployeePhone()));

                            if (ActivityCompat.checkSelfPermission(view1.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            view1.getContext().startActivity(intent);
                        }
                    });
                    staffEmail.setText(form.getEmployeeEmail());

                    builder.setView(view1);
                    AlertDialog alertDialog= builder.create();

                    alertDialog.show();


                Intent intent= new Intent(view.getContext(), Staff_Infro.class);
                    intent.putExtra("staffId",form.getEmployeeName());
                    view.getContext().startActivity(intent);
                    Toast.makeText(view.getContext(), form.getEmployeeName(), Toast.LENGTH_SHORT).show();*/
                }
            });
        }
    }
    @Override
    public Mholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=activity.getLayoutInflater();
        View view= layoutInflater.inflate(R.layout.staff_recycle,parent,false);
        return new  Mholder(view);
    }

    @Override
    public void onBindViewHolder(StaffAdapter.Mholder holder, int position) {
       RegistrationForm getStaffId= forms.get(position);
        holder.staffName.setText(forms.get(position).getEmployeeName());
        holder.staffPhone.setText(forms.get(position).getEmployeePhone());
        holder.staffDepartment.setText(forms.get(position).getEmployeeDepartment());
        holder.form=getStaffId;

        String firstletter= String.valueOf(getStaffId.getEmployeeName().charAt(0));
        ColorGenerator generator= ColorGenerator.MATERIAL;
        int color= generator.getColor(getStaffId);
        TextDrawable drawable= TextDrawable.builder().buildRound(firstletter, color);
        holder.imageView.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return forms.size();
    }
}
