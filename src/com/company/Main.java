package com.company;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Map<String, Pessoa> terminal = new HashMap<String,Pessoa>();
        Map<String, Pessoa> aviao = new HashMap<String,Pessoa>();
        String motorista = "";
        String carona = "";

        terminal.put("Piloto", new Pessoa("Piloto"));
        terminal.put("Oficial 1", new Pessoa("Oficial 1"));
        terminal.put("Oficial 2", new Pessoa("Oficial 2"));
        terminal.put("Chefe de servico", new Pessoa("Chefe de servico"));
        terminal.put("Comissaria 1", new Pessoa("Comissaria 1"));
        terminal.put("Comissaria 2", new Pessoa("Comissaria 2"));
        terminal.put("Policial", new Pessoa("Policial"));
        terminal.put("Presidiario", new Pessoa("Presidiario"));

        try {
            while (!terminal.isEmpty()) {
                printLocal(terminal, aviao);

                System.out.println("\nINDO DO TERMINAL AO AVIAO");

                motorista = setMotorista(motorista, terminal);
                carona = setCarona(motorista, carona, terminal);

                if (checkRestricoesIda(terminal, aviao, motorista, carona)){
                    System.out.println("INDO AO AVIAO COM " + motorista + " E " + carona + "\n");
                    motorista = "";
                    carona = "";
                    for (int i = 0; i < 6; i++) {
                        System.out.println("|");
                        Thread.sleep(500);
                    }
                    boolean existeRestricao;

                    printLocal(terminal, aviao);
                    System.out.println("\n");

                    if (aviao.size() < 8) {
                        do {
                            existeRestricao = false;
                            System.out.println("INDO DO AVIAO AO TERMINAL");

                            motorista = setMotorista(motorista, aviao);
                            carona = setCarona(motorista, carona, aviao);

                            if (checkRestricoesVolta(terminal, aviao, motorista, carona)) {
                                System.out.println("INDO AO TERMINAL COM " + motorista + " E " + carona + "\n");
                                for (int i = 0; i < 6; i++) {
                                    System.out.println("|");
                                    Thread.sleep(500);
                                }

                                motorista = "";
                                carona = "";
                            } else {
                                System.out.println("IMPOSSIVEL REALIZAR VIAGEM DE VOLTA");
                                existeRestricao = true;
                                motorista = "";
                                carona = "";
                            }
                        } while (existeRestricao);
                    }
                } else {
                    System.out.println("IMPOSSIVEL REALIZAR VIAGEM");
                }

                motorista = "";
                carona = "";
            }
            System.out.println("EMBARQUE CONCLUIDO");
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    public static void printLocal(Map<String, Pessoa> terminal, Map<String, Pessoa> aviao) {
        System.out.println("NO TERMINAL:");
        for (Map.Entry<String, Pessoa> entry : terminal.entrySet()) {
            System.out.println(entry.getValue().getNome());
        }

        System.out.println("");

        System.out.println("NO AVIAO:");
        for (Map.Entry<String, Pessoa> entry : aviao.entrySet()) {
            System.out.println(entry.getValue().getNome());
        }
    }

    public static String setMotorista(String motorista, Map<String, Pessoa> local) {
        Scanner entrada = new Scanner(System.in);
        while (motorista.equals("")) {
            System.out.println("Digite o nome do motorista:");
            motorista = entrada.nextLine();

            if (motorista.equals("Piloto") || motorista.equals("Chefe de servico") || motorista.equals("Policial")) {
                Pessoa p = local.get(motorista);
                if (p == null) {
                    motorista = "";
                }
            } else {
                motorista = "";
            }
        }

        return motorista;
    }

    public static String setCarona(String motorista, String carona, Map<String, Pessoa> local) {
        Scanner entrada = new Scanner(System.in);
        while (carona.equals("")) {
            System.out.println("Digite o nome do carona ou a palavra Vazio caso o motorista vá sozinho:");
            carona = entrada.nextLine();

            if (!carona.equals("Vazio")) {
                Pessoa p = local.get(carona);
                if (p == null || motorista.equals(carona)) {
                    carona = "";
                }
            }
        }

        return carona;
    }

    public static boolean checkRestricoesIda(Map<String, Pessoa> terminal_, Map<String, Pessoa> aviao_, String motorista_, String carona_) {
        // Remove o motorista do terminal e coloca no aviao
        Pessoa motoristaAceito = terminal_.remove(motorista_);
        aviao_.put(motorista_, motoristaAceito);

        // Remove o carona do terminal e coloca no avião
        Pessoa caronaAceito = null;
        if(!carona_.equals("Vazio")) {
            caronaAceito = terminal_.remove(carona_);
            aviao_.put(carona_, caronaAceito);
        }

        // Verifica as pessoas no veiculo. Se houver restriçao, retorna false
        if(!restricoesVeiculo(motorista_, carona_)){
            motoristaAceito = aviao_.remove(motorista_);
            terminal_.put(motorista_, motoristaAceito);

            if(!carona_.equals("Vazio")) {
                caronaAceito = aviao_.remove(carona_);
                terminal_.put(carona_, caronaAceito);
            }
            return false;
        }

        // Verifica se piloto está sozinho com comissaria, chefe com oficial ou presidiario sem policial
        if (terminal_.size() == 2) {
            if (restricoesTerminal(terminal_)) {
                return true;
            } else {
                // Se houver restrição retorna false
                motoristaAceito = aviao_.remove(motorista_);
                terminal_.put(motorista_, motoristaAceito);

                if(!carona_.equals("Vazio")) {
                    caronaAceito = aviao_.remove(carona_);
                    terminal_.put(carona_, caronaAceito);
                }
                return false;
            }
        }
        return true;
    }

    public static boolean checkRestricoesVolta(Map<String, Pessoa> terminal_, Map<String, Pessoa> aviao_, String motorista_, String carona_) {
        // Remove o motorista do aviao e coloca no terminal
        Pessoa motoristaAceito = aviao_.remove(motorista_);
        terminal_.put(motorista_, motoristaAceito);

        // Remove o carona do terminal e coloca no avião
        Pessoa caronaAceito = null;
        if(!carona_.equals("Vazio")) {
            caronaAceito = aviao_.remove(carona_);
            terminal_.put(carona_, caronaAceito);
        }

        // Verifica as pessoas no veiculo. Se houver restriçao, retorna false
        if(!restricoesVeiculo(motorista_, carona_)){
            motoristaAceito = terminal_.remove(motorista_);
            aviao_.put(motorista_, motoristaAceito);

            if(!carona_.equals("Vazio")) {
                caronaAceito = terminal_.remove(carona_);
                aviao_.put(carona_, caronaAceito);
            }
            return false;
        }

        // Verifica se piloto está sozinho com comissaria, chefe com oficial ou presidiario sem policial
        if (aviao_.size() == 2) {
            if (restricoesAviao(aviao_)) {
                return true;
            } else {
                // Se houver restrição retorna false
                motoristaAceito = terminal_.remove(motorista_);
                aviao_.put(motorista_, motoristaAceito);

                if(!carona_.equals("Vazio")) {
                    caronaAceito = terminal_.remove(carona_);
                    aviao_.put(carona_, caronaAceito);
                }
                return false;
            }
        }
        return true;
    }

    public static boolean restricoesTerminal(Map<String, Pessoa> terminal){
        if (terminal.get("Piloto") != null && terminal.get("Comissaria 1") != null) {
            System.out.println("O piloto ficou sozinho com a comissaria 1");
            return false;
        }

        if (terminal.get("Piloto") != null && terminal.get("Comissaria 2") != null) {
            System.out.println("O piloto ficou sozinho com a comissaria 2");
            return false;
        }

        if (terminal.get("Chefe de servico") != null && terminal.get("Oficial 1") != null) {
            System.out.println("O chefe de servico ficou sozinho com o oficial 1");
            return false;
        }

        if (terminal.get("Chefe de servico") != null && terminal.get("Oficial 2") != null) {
            System.out.println("O chefe de servico ficou sozinho com o oficial 2");
            return false;
        }

        if (terminal.get("Policial") != null && terminal.get("Presidiario") == null) {
            System.out.println("Alguem pegou o presidiario como carona");
            return false;
        }

        if (terminal.get("Presidiario") != null && terminal.get("Policial") == null) {
            System.out.println("O policial saiu sem o presidiario");
            return false;
        }

        return true;
    }

    public static boolean restricoesAviao(Map<String, Pessoa> aviao){
        if (aviao.get("Piloto") != null && aviao.get("Comissaria 1") != null) {
            System.out.println("O Piloto ficou sozinho com a Comissaria 1");
            return false;
        }

        if (aviao.get("Piloto") != null && aviao.get("Comissaria 2") != null) {
            System.out.println("O Piloto ficou sozinho com a Comissaria 2");
            return false;
        }

        if (aviao.get("Chefe de servico") != null && aviao.get("Oficial 1") != null) {
            System.out.println("O Chefe de servico ficou sozinho com a Oficial 1");
            return false;
        }

        if (aviao.get("Chefe de servico") != null && aviao.get("Oficial 2") != null) {
            System.out.println("O Chefe de servico ficou sozinho com a Oficial 2");
            return false;
        }

        if (aviao.get("Policial") != null && aviao.get("Presidiario") == null) {
            System.out.println("Alguem pegou o presidiario como carona");
            return false;
        }

        if (aviao.get("Presidiario") != null && aviao.get("Policial") == null) {
            System.out.println("O policial saiu sem o presidiario");
            return false;
        }

        return true;
    }

    public static boolean restricoesVeiculo(String motorista, String carona) {
        if (motorista.equals("Piloto") && carona.equals("Comissaria 1")) {
            System.out.println("Piloto está sozinho com a comissaria 1");
            return false;
        }

        if (motorista.equals("Piloto") && carona.equals("Comissaria 2")) {
            System.out.println("Piloto está sozinho com a comissaria 2");
            return false;
        }

        if (motorista.equals("Chefe de servico") && carona.equals("Oficial 1")) {
            System.out.println("Chefe de servico está sozinho com o oficial 1");
            return false;
        }

        if (motorista.equals("Chefe de servico") && carona.equals("Oficial 2")) {
            System.out.println("Chefe de servico está sozinho com o oficial 2");
            return false;
        }

        if (motorista.equals("Policial") && !carona.equals("Presidiario")) {
            System.out.println("Policial nao pode partir sem o presidiario");
            return false;
        }

        if (!motorista.equals("Policial") && carona.equals("Presidiario")) {
            System.out.println("Policial nao pode partir sem o presidiario");
            return false;
        }
        return true;
    }
}
