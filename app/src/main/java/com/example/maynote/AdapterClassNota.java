package com.example.maynote;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterClassNota extends RecyclerView.Adapter<AdapterClassNota.ViewHolder> {

    private final RecyclerNota_Interface recyclerNota_interface;
    public List<ModelClassNota> notaList;

    AdapterClassNota(RecyclerNota_Interface recyclerNota_interface, List<ModelClassNota> notaList) {
        this.recyclerNota_interface = recyclerNota_interface;
        this.notaList = notaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewnota_item, parent,false),recyclerNota_interface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String titulo = notaList.get(position).getTitle();
        String descricao = notaList.get(position).getDescription();
        String data = notaList.get(position).getDate();

        holder.setData(titulo,descricao,data);
    }

    @Override
    public int getItemCount() {
        return notaList.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, description, date;
        public ViewHolder(@NonNull View itemView, RecyclerNota_Interface nota_interface) {
            super(itemView);
            title = itemView.findViewById(R.id.textNotasTitle);
            description = itemView.findViewById(R.id.textNotasDescription);
            date = itemView.findViewById(R.id.textNotasDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerNota_interface!=null){
                        int pos = getLayoutPosition();
                        if (pos!=RecyclerView.NO_POSITION){
                            recyclerNota_interface.onItemClick(pos);
                        }
                    }
                }
            });
        }

        public void setData(String titulo, String descricao, String data) {
            title.setText(titulo);
            description.setText(descricao);
            date.setText(data);
        }
    }
}
