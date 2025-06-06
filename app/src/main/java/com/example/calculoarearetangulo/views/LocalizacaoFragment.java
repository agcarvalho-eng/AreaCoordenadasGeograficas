package com.example.calculoarearetangulo.views;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.calculoarearetangulo.databinding.FragmentLocalizacaoBinding;

// Classe LocalizacaoFragment que estende Fragment
public class LocalizacaoFragment extends Fragment {

    // Código de requisição usado para identificar a solicitação de permissão de localização
    private static final int REQUEST_CODE = 100;

    // ViewModel que gerencia os dados e lógica de localização
    private LocalizacaoViewModel viewModel;

    // Binding gerado automaticamente para acessar os componentes da interface (View Binding)
    private FragmentLocalizacaoBinding binding;

    // Método chamado para inflar o layout do fragmento e inicializar a interface
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Infla o layout do fragmento usando ViewBinding
        binding = FragmentLocalizacaoBinding.inflate(inflater, container, false);

        // Inicializa o ViewModel associado ao fragmento
        viewModel = new ViewModelProvider(this).get(LocalizacaoViewModel.class);

        // Passa o binding para o ViewModel, permitindo acesso direto aos componentes da interface
        viewModel.setBinding(binding);

        // Define ação do botão para definir o ponto 1, verificando permissões e obtendo localização
        binding.btnSetPonto1.setOnClickListener(v -> checkPermissionAndGetLocation(true));

        // Define ação do botão para definir o ponto 2
        binding.btnSetPonto2.setOnClickListener(v -> checkPermissionAndGetLocation(false));

        // Observa o LiveData da área e atualiza o texto da interface quando o valor mudar
        viewModel.area.observe(getViewLifecycleOwner(), area ->
                binding.textArea.setText(String.format("Área: %.2f m²", area))
        );

        // Retorna a raiz do layout inflado
        return binding.getRoot();
    }

    // Método que verifica se a permissão de localização foi concedida e, se sim, obtém a localização
    private void checkPermissionAndGetLocation(boolean isPonto1) {
        // Verifica se a permissão de localização fina está concedida
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Solicita a permissão de localização ao usuário
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            // Se a permissão já foi concedida, chama o método do ViewModel para obter a localização
            viewModel.getLocation(requireContext(), isPonto1);
        }
    }

    // Método chamado automaticamente quando o usuário responde à solicitação de permissão
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Chama a implementação padrão
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Verifica se a resposta foi para a requisição de permissão esperada e se foi concedida
        if (requestCode == REQUEST_CODE && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // Mostra mensagem de permissão concedida
            Toast.makeText(getContext(), "Permissão concedida", Toast.LENGTH_SHORT).show();
        } else {
            // Mostra mensagem de permissão negada
            Toast.makeText(getContext(), "Permissão negada", Toast.LENGTH_SHORT).show();
        }
    }
}
