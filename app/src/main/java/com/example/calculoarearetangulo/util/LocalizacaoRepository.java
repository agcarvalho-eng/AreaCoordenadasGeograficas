package com.example.calculoarearetangulo.util;

import android.content.Context;
import android.location.Location;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;

// Classe que gerencia a localização de dois pontos e o cálculo da área
public class LocalizacaoRepository {

    // Variáveis privadas que armazenam os dois pontos geográficos
    private Location ponto1, ponto2;

    // Interface de callback usada para retornar a localização obtida de forma assíncrona
    public interface LocationCallback {
        void onLocationReceived(Location location);
    }

    // Método que obtém a localização atual do dispositivo
    public void getCurrentLocation(Context context, LocationCallback callback) {
        // Cria uma instância do cliente de localização do Google usando o contexto da aplicação
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);

        // Cria uma requisição para obter a localização atual com alta precisão
        CurrentLocationRequest request = new CurrentLocationRequest.Builder()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build();

        // Solicita a localização atual com a requisição e um token de cancelamento
        client.getCurrentLocation(request, new CancellationTokenSource().getToken())
                // Define o que fazer quando a localização for obtida com sucesso
                .addOnSuccessListener(location -> {
                    // Se a localização não for nula, passa-a para o callback
                    if (location != null) {
                        callback.onLocationReceived(location);
                    } else {
                        // Caso contrário, retorna nulo pelo callback
                        callback.onLocationReceived(null);
                    }
                })
                // Define o que fazer caso haja erro ao obter a localização
                .addOnFailureListener(e -> callback.onLocationReceived(null));
    }

    // Define o valor do ponto1 com um objeto Location
    public void setPonto1(Location location) {
        ponto1 = location;
    }

    // Define o valor do ponto2 com um objeto Location
    public void setPonto2(Location location) {
        ponto2 = location;
    }

    // Retorna o valor armazenado em ponto1
    public Location getPonto1() {
        return ponto1;
    }

    // Retorna o valor armazenado em ponto2
    public Location getPonto2() {
        return ponto2;
    }

    // Calcula a área de um retângulo formado por dois pontos geográficos (Haversine)
    public Double calcularArea() {
        // Verifica se ambos os pontos foram definidos
        if (ponto1 != null && ponto2 != null) {
            // Verifica se os dois pontos são o mesmo (distância zero)
            if (ponto1.distanceTo(ponto2) == 0) {
                return 0.0;
            }

            // Cria um ponto auxiliar com a latitude de ponto2 e longitude de ponto1
            Location localizacaoLat = new Location("");
            localizacaoLat.setLatitude(ponto2.getLatitude());
            localizacaoLat.setLongitude(ponto1.getLongitude());

            // Cria um ponto auxiliar com a latitude de ponto1 e longitude de ponto2
            Location localizacaoLon = new Location("");
            localizacaoLon.setLatitude(ponto1.getLatitude());
            localizacaoLon.setLongitude(ponto2.getLongitude());

            // Calcula a distância vertical (altura) entre ponto1 e localizacaoLat
            double altura = ponto1.distanceTo(localizacaoLat);

            // Calcula a distância horizontal (base) entre ponto1 e localizacaoLon
            double base = ponto1.distanceTo(localizacaoLon);

            // Retorna a área do retângulo (base x altura)
            return base * altura;
        }
        // Se os pontos não forem definidos, retorna área zero
        return 0.0;
    }

//    // Calcula a área de um retângulo formado por dois pontos geográficos (sem Haversine)
//    public Double calcularArea() {
//        // Verifica se ambos os pontos foram definidos
//        if (ponto1 != null && ponto2 != null) {
//            // Verifica se os dois pontos são o mesmo (área zero)
//            if (ponto1.getLatitude() == ponto2.getLatitude() &&
//                    ponto1.getLongitude() == ponto2.getLongitude()) {
//                return 0.0;
//            }
//
//            // Obtém as coordenadas dos pontos
//            double lat1 = ponto1.getLatitude();
//            double lon1 = ponto1.getLongitude();
//            double lat2 = ponto2.getLatitude();
//            double lon2 = ponto2.getLongitude();
//
//            // Calcula as diferenças de latitude e longitude em graus
//            double deltaLat = Math.abs(lat1 - lat2);
//            double deltaLon = Math.abs(lon1 - lon2);
//
//            // Conversão aproximada de graus para metros
//            // 1 grau de latitude ≈ 111.32 km (constante)
//            double altura = deltaLat * 111320;
//
//            // 1 grau de longitude varia com a latitude (usamos latitude média para cálculo)
//            double latitudeMedia = Math.toRadians((lat1 + lat2) / 2);
//            double base = deltaLon * (111320 * Math.cos(latitudeMedia));
//
//            // Retorna a área do retângulo (base x altura)
//            return base * altura;
//        }
//        // Se os pontos não forem definidos, retorna área zero
//        return 0.0;
//    }
}
