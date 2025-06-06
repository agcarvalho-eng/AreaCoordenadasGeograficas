package com.example.calculoarearetangulo.views;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.calculoarearetangulo.R;
import com.example.calculoarearetangulo.databinding.ActivityMainBinding;

// Classe MainActivity que herda de AppCompatActivity
public class MainActivity extends AppCompatActivity {

    // Objeto de binding gerado automaticamente para acessar os componentes da UI
    ActivityMainBinding binding;

    // Método chamado quando a atividade é criada
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Chama o método da superclasse para manter o ciclo de vida da atividade

        // Infla o layout da atividade usando ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // Define o layout inflado como a visualização principal da atividade
        setContentView(binding.getRoot());

        // Carrega o fragmento de localização como conteúdo inicial dentro do container definido no layout
        loadFragment(new LocalizacaoFragment());
    }

    // Método auxiliar para carregar dinamicamente um fragmento dentro do container
    private void loadFragment(Fragment fragment) {
        // Inicia uma transação de fragmento, substitui o fragmento atual pelo fornecido e finaliza a transação
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment) // Substitui o conteúdo do container com o novo fragmento
                .commit(); // Finaliza a transação
    }
}
