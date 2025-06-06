package com.example.calculoarearetangulo.views;

import android.content.Context;
import android.widget.Toast;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.calculoarearetangulo.databinding.FragmentLocalizacaoBinding;
import com.example.calculoarearetangulo.util.LocalizacaoRepository;

// Classe LocalizacaoViewModel que estende ViewModel
public class LocalizacaoViewModel extends ViewModel {

    // Instância do repositório responsável por obter localizações e calcular a área
    private final LocalizacaoRepository repository = new LocalizacaoRepository();

    // LiveData mutável que armazena o valor da área calculada
    private final MutableLiveData<Double> MutArea = new MutableLiveData<>();

    // LiveData pública e imutável usada para observar mudanças na área a partir da interface
    public LiveData<Double> area = MutArea;

    // Referência ao binding da interface para acessar e atualizar elementos de UI
    private FragmentLocalizacaoBinding binding;

    // Método para injetar o binding vindo do Fragment e permitir o acesso aos componentes visuais
    public void setBinding(FragmentLocalizacaoBinding binding) {
        this.binding = binding;
    }

    // Método responsável por obter a localização atual e definir ponto 1 ou 2, conforme indicado
    public void getLocation(Context context, boolean isPonto1) {
        // Chama o método do repositório que obtém a localização atual do dispositivo
        repository.getCurrentLocation(context, location -> {

            // Verifica se a localização foi obtida com sucesso
            if (location == null) {
                // Mostra uma mensagem de erro se a localização estiver nula
                Toast.makeText(context, "Não foi possível obter a localização",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Caso seja para definir o Ponto 1
            if (isPonto1) {
                // Armazena a localização como Ponto 1 no repositório
                repository.setPonto1(location);

                // Atualiza o texto na interface com as coordenadas do Ponto 1
                binding.textPonto1.setText(String.format("P1: %.6f, %.6f",
                        location.getLatitude(),
                        location.getLongitude()));

                // Mostra um Toast informando que o Ponto 1 foi definido
                Toast.makeText(context, "Ponto 1 definido", Toast.LENGTH_SHORT).show();

            } else {
                // Se for para definir o Ponto 2, primeiro verifica se o Ponto 1 já foi definido
                if (repository.getPonto1() == null) {
                    // Mostra mensagem de erro se o Ponto 1 ainda não estiver definido
                    Toast.makeText(context, "Defina o Ponto 1 primeiro",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Armazena a localização como Ponto 2 no repositório
                repository.setPonto2(location);

                // Atualiza o texto na interface com as coordenadas do Ponto 2
                binding.textPonto2.setText(String.format("P2: %.6f, %.6f",
                        location.getLatitude(),
                        location.getLongitude()));

                // Calcula a área com os dois pontos definidos e atualiza o LiveData
                MutArea.postValue(repository.calcularArea());
            }
        });
    }
}

