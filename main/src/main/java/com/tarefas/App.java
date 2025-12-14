package com.tarefas;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GerenciadorTarefas gerenciador = new GerenciadorTarefas();
        int op = -1;

        /*
         * Criando um menu simples para realização das seguintes atividades:
         * 1. Criar tarefa (descrição, prazo, prioridade).
         * 2. Registrar dependência: definir que uma tarefa depende de outra (pai →
         * filho).
         * 3. Remover tarefa: deve remover todas as subtarefas dependentes.
         * 4. Verificar “caminho de dependência” de uma tarefa - mostrar sequência desde
         * tarefa
         * raiz até ela.
         * 5. Listar todas as tarefas sem dependências (raízes), ou todas folhas
         * (tarefas finais).
         * 6. Mostrar estrutura de tarefas em formato de árvore.
         */
        while (op != 0) {
            System.out.println("\n--- MENU --- ");
            System.out.println("Escolha o que deseja realizar:");
            System.out.println("1. Criar nova tarefa");
            System.out.println("2. Registrar uma tarefa dependente de outra");
            System.out.println("3. Remover Tarefa");
            System.out.println("4. Mostrar caminhos da tarefa principal até a tarefa dependente");
            System.out.println("5. Listar todas as tarefas sem dependência");
            System.out.println("6. Listar todas as tarefas dependentes (finais)");
            System.out.println("7. Mostrar a estrutura de tarefas em formato de árvore");
            System.out.println("0. Encerrar o Programa");
            op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1:
                    gerenciador.newTarefa();
                    break;
                case 2:
                    gerenciador.registrarDependencia();
                    break;
                case 3:
                    gerenciador.removerTarefa();
                    break;
                case 4:
                    gerenciador.verificarCaminhoDependencia();
                    break;
                case 5:
                    gerenciador.mostrarRaizes();
                    break;
                case 6:
                    gerenciador.mostrarFolhas();
                    break;
                case 7:
                    

                    break;
                case 0:
                    System.out.println("Encerrando o programa");

                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        }
    }
}
