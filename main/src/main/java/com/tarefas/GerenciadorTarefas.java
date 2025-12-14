package com.tarefas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
 * Classe responsável por armazenar e gerenciar todas as tarefas criadas.
 * Funciona como uma espécie de "banco de dados" em memória.
 */
public class GerenciadorTarefas {

    private Scanner sc = new Scanner(System.in);
    private Map<Integer, Tarefa> tarefas;
    private int contadorId;

    /*
     * Construtor: inicia o Map vazio e define o primeiro ID como 1.
     */
    public GerenciadorTarefas() {
        this.tarefas = new HashMap<>();
        this.contadorId = 1;
    }

    /*
     * Cria uma nova tarefa com ID automático
     * Desenvolvido por: Luara Araújo
     */
    public Tarefa criarTarefa(String descricao, String prazo, int prioridade) {
        int id = contadorId++;
        Tarefa novaTarefa = new Tarefa(id, descricao, prazo, prioridade);
        tarefas.put(id, novaTarefa);
        return novaTarefa;
    }

    /*
     * Método interativo para criação de tarefa
     * Desenvolvido por: Luara Araújo
     */
    public void newTarefa() {

        System.out.println("-- CRIANDO TAREFA --");

        // Descrição
        System.out.print("DESCREVA COMO SERÁ A SUA TAREFA: ");
        String descricao = sc.nextLine();

        if (descricao.trim().isEmpty()) {
            System.out.println("Erro: A descrição não pode estar vazia.");
            return;
        }

        // Prazo
        String prazo = "";
        while (prazo.isEmpty()) {
            System.out.print("DIGITE O PRAZO (dd/mm/aaaa): ");
            prazo = sc.nextLine();

            if (!prazo.matches("\\d{2}/\\d{2}/\\d{4}")) {
                System.out.println("Formato inválido. Use dd/mm/aaaa.");
                prazo = "";
            }
        }

        // Prioridade
        int prioridade = -1;
        while (prioridade < 1 || prioridade > 10) {
            try {
                System.out.print("DIGITE A PRIORIDADE (1 até 10): ");
                prioridade = sc.nextInt();
                sc.nextLine(); // limpa buffer

                if (prioridade < 1 || prioridade > 10) {
                    System.out.println("A prioridade deve ser de 1 até 10.");
                }
            } catch (Exception e) {
                System.out.println("Erro: Por favor, insira um número válido.");
                sc.nextLine(); // limpa buffer
            }
        }

        criarTarefa(descricao, prazo, prioridade);
        System.out.println("-- TAREFA CRIADA COM SUCESSO --");
    }

    /*
     * ---------------------------------------------------------
     * FUNCIONALIDADE 2: REGISTRAR DEPENDENCIA
     * DESENVOLVIDO POR: RAIANE MELGACO
     * ---------------------------------------------------------
     */
    public void registrarDependencia() {
        System.out.println("-- REGISTRAR DEPENDENCIA --");

        for (Tarefa t : tarefas.values()) {
            System.out.println("ID: " + t.getId() + " - " + t.getDescricao());
        }

        try {
            System.out.print("DIGITE O ID DA TAREFA PAI: ");
            int idPai = sc.nextInt();

            System.out.print("DIGITE O ID DA TAREFA FILHO: ");
            int idFilho = sc.nextInt();
            sc.nextLine();

            if (idPai == idFilho) {
                System.out.println("Erro: IDs iguais.");
                return;
            }

            Tarefa pai = tarefas.get(idPai);
            Tarefa filho = tarefas.get(idFilho);

            if (pai == null || filho == null) {
                System.out.println("Erro: Tarefa nao encontrada.");
                return;
            }

            pai.adicionarFilho(filho);
            System.out.println("Dependencia registrada com sucesso !");

        } catch (Exception e) {
            System.out.println("Erro: Voce deve digitar apenas numeros inteiros!");
            sc.nextLine(); // Limpa o buffer do scanner para nao travar
        }
    }

    /*
     * 3 - Remover Tarefa
     * Remove a tarefa selecionada e todas as suas subtarefas dependentes
     *
     * Desenvolvido por: Ana Clara Costa Sousa
     */
    public void removerTarefa() {
        System.out.println("\n-- REMOVER TAREFA --");
        System.out.print("DIGITE O ID DA TAREFA QUE DESEJA REMOVER: ");

        try {
            int id = sc.nextInt();
            sc.nextLine(); // Limpa buffer

            Tarefa tarefaAlvo = tarefas.get(id);

            if (tarefaAlvo == null) {
                System.out.println("Erro: Tarefa com ID " + id + " não encontrada.");
                return;
            }

            // AVISO DE SEGURANÇA
            if (!tarefaAlvo.getFilhos().isEmpty()) {
                System.out.println("AVISO: Esta tarefa possui " + tarefaAlvo.getFilhos().size() + " subtarefas.");
                System.out.println("Removê-la irá apagar TODAS as tarefas dependentes dela.");
                System.out.print("Tem certeza? (S/N): ");
                String confirmacao = sc.nextLine();
                if (!confirmacao.equalsIgnoreCase("S")) {
                    System.out.println("Operação cancelada.");
                    return;
                }
            }

            // 1- Desconectar do pai, se houver
            if (tarefaAlvo.getPai() != null) {
                tarefaAlvo.getPai().getFilhos().remove(tarefaAlvo);
            }

            // 2- Remover recursivamente
            removerRecursivamente(tarefaAlvo);

            System.out.println("Tarefa e dependentes removidos com sucesso.");

        } catch (Exception e) {
            System.out.println("Erro: Entrada inválida. Digite apenas números.");
            sc.nextLine();
        }
    }

    private void removerRecursivamente(Tarefa t) {
        for (Tarefa filho : t.getFilhos()) {
            removerRecursivamente(filho);
        }
        tarefas.remove(t.getId());
    }

    /*
     * 4 - Verificar caminho de dependência
     * Mostra a sequência da Raiz até a tarefa selecionada
     * 
     * Desenvolvido por: Lorena Pereira
     */
    public void verificarCaminhoDependencia() {
        System.out.println("\n-- VERIFICAR CAMINHO DE DEPENDÊNCIA --");

        System.out.print("Digite o ID da tarefa: ");
        int idBuscado;
        try {
            idBuscado = sc.nextInt();
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("Erro: ID inválido");
            sc.nextLine();
            return;
        }

        // Busca a tarefa no Map (O(1))
        Tarefa tarefaAtual = tarefas.get(idBuscado);

        if (tarefaAtual == null) {
            System.out.println("Erro: Tarefa com ID " + idBuscado + " não encontrada");
            return;
        }

        List<String> caminho = new ArrayList<>();

        // Subida, vai do filho para o pai até o pai ser null
        Tarefa temp = tarefaAtual;
        while (temp != null) {
            caminho.add("[" + temp.getId() + "] " + temp.getDescricao());
            temp = temp.getPai(); // sobe um nível na árvore
        }

        // A lista está invertida (Filho -> Pai -> Raiz)
        // Usamos Collections do Java para inverter (Raiz -> Pai -> Filho)
        Collections.reverse(caminho);

        // Printa o caminho
        System.out.println("\nCaminho encontrado:");
        System.out.println(String.join(" -> ", caminho));
    }
   /*
 * ---------------------------------------------------------
 * FUNCIONALIDADE 5: LISTAR RAÍZES E FOLHAS
 * DESENVOLVIDO POR: Nickolas Gabriel
 * ---------------------------------------------------------
 */

/*
 * Retorna todas as tarefas que não possuem tarefa "pai" (Raízes).
 */
public List<Tarefa> listarRaizes() {
    List<Tarefa> raizes = new ArrayList<>();
    
    // Itera sobre todas as tarefas armazenadas no HashMap (tarefas.values())
    for (Tarefa tarefa : tarefas.values()) {
        // Uma raiz é uma tarefa que não tem pai
        if (tarefa.getPai() == null) {
            raizes.add(tarefa);
        }
    }
    return raizes;
}

/*
 * Retorna todas as tarefas que não possuem subtarefas (Folhas / Finais).
 */
public List<Tarefa> listarFolhas() {
    List<Tarefa> folhas = new ArrayList<>();

    // Itera sobre todas as tarefas armazenadas no HashMap (tarefas.values())
    for (Tarefa tarefa : tarefas.values()) {
        // Uma folha é uma tarefa cuja lista de filhos está vazia
        if (tarefa.getFilhos().isEmpty()) {
            folhas.add(tarefa);
        }
    }
    return folhas;
}

public void mostrarRaizes() {
    List<Tarefa> raizes = listarRaizes();
    System.out.println("\n--- TAREFAS SEM DEPENDÊNCIAS (RAÍZES) ---");
    if (raizes.isEmpty()) {
        System.out.println("Nenhuma tarefa raiz encontrada.");
    } else {
        raizes.forEach(t -> System.out.println("ID " + t.getId() + ": " + t.getDescricao() + " | Prioridade: " + t.getPrioridade()));
    }
}

/*
 * Método interativo para mostrar as tarefas folhas.
 */
public void mostrarFolhas() {
    List<Tarefa> folhas = listarFolhas();
    System.out.println("\n--- TAREFAS FINAIS (FOLHAS) ---");
    if (folhas.isEmpty()) {
        System.out.println("Nenhuma tarefa folha encontrada.");
    } else {
        folhas.forEach(t -> System.out.println("ID " + t.getId() + ": " + t.getDescricao() + " | Prioridade: " + t.getPrioridade()));
    }
}
}
