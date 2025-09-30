package com.sustria.codcoz.ui.perfil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PerfilViewModel extends ViewModel {
    private final MutableLiveData<String> nome = new MutableLiveData<>("Filipe Inácio");
    private final MutableLiveData<String> funcao = new MutableLiveData<>("estoquista .@");
    private final MutableLiveData<String> desde = new MutableLiveData<>("Estoque desde 14/05/2024");

    public LiveData<String> getNome() { return nome; }
    public LiveData<String> getFuncao() { return funcao; }
    public LiveData<String> getDesde() { return desde; }
}


