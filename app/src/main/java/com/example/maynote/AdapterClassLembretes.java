package com.example.maynote;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterClassLembretes extends RecyclerView.Adapter<AdapterClassLembretes.ViewHolder> {

    private final RecyclerLembretes_Interface recyclerLembretes_interface;
    public List<ModelClassLembretes> lembretesList;

    AdapterClassLembretes(RecyclerLembretes_Interface recyclerLembretes_interface, List<ModelClassLembretes> lembretesList) {
        this.recyclerLembretes_interface = recyclerLembretes_interface;
        this.lembretesList = lembretesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewlembretes_item, parent,false), recyclerLembretes_interface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String titulo = lembretesList.get(position).getTitle();
        String data = lembretesList.get(position).getDate();
        String hora = lembretesList.get(position).getHour();

        holder.setData(titulo,data,hora);
    }

    @Override
    public int getItemCount() {
        return lembretesList.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView date;
        private final TextView hour;
        public ViewHolder(@NonNull View itemView, RecyclerLembretes_Interface nota_interface) {
            super(itemView);
            title = itemView.findViewById(R.id.textLembretesTitle);
            date = itemView.findViewById(R.id.textLembretesData);
            hour = itemView.findViewById(R.id.textLembretesHora);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerLembretes_interface!=null){
                        int pos = getLayoutPosition();
                        if (pos!=RecyclerView.NO_POSITION){
                            recyclerLembretes_interface.onItemClick(pos);
                        }
                    }
                }
            });
        }

        public void setData(String titulo, String data, String hora) {
            title.setText(titulo);
            date.setText(data);
            hour.setText(hora);
        }
    }
}
