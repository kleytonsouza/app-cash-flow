# FinanceFlow 📱💸

> Aplicativo nativo para Android desenvolvido em Kotlin para controle e fluxo de caixa pessoal. Trabalho prático desenvolvido para a Especialização em Programação para Dispositivos Móveis da **UTFPR (Universidade Tecnológica Federal do Paraná - Campus Pato Branco)**.

O **App Cash Flow** é um gerenciador financeiro simples, direto e robusto que permite realizar o cadastro de movimentações diárias (Receitas e Despesas), persistindo as informações em um banco de dados local SQLite e exibindo um extrato financeiro detalhado com cálculo de saldo dinâmico.

---

## 🚀 Funcionalidades

### 1. Tela de Cadastro (Lançamentos)
*   **Campos Requeridos**:
    *   **Valor**: Campo numérico formatado com prefixo monetário dinâmico.
    *   **Descrição**: Texto explicativo do lançamento (ex: "Aluguel", "Salário").
    *   **Data**: Seleção fácil via calendário nativo `DatePickerDialog` integrado.
    *   **Tipo**: Seleção entre *Receita (Crédito)* ou *Despesa (Débito)* via botões do tipo `RadioButton`.
*   **Validação de Inputs**: O app impede gravações inválidas (campos vazios ou valores menores/iguais a zero) exibindo alertas diretamente nos campos com o padrão de erros do Material Design.
*   **Ciclo de Edição e Exclusão**: É possível editar ou excluir lançamentos já salvos ao clicar no ícone de lápis na listagem.

### 2. Tela de Extrato (Fluxo de Caixa)
*   **Listagem Customizada**: Exibição dos lançamentos ordenados por data em uma `ListView` customizada com o padrão de cards elevados.
*   **Diferenciação Visual**: 
    *   Itens de **Receita** são identificados com uma seta verde apontada para cima (`▲`) e valor com prefixo `+`.
    *   Itens de **Despesa** são destacados com uma seta vermelha apontada para baixo (`▼`) e valor com prefixo `-`.
*   **Saldo Acumulado Dinâmico**: Um card fixado no topo exibe o saldo geral das transações (Soma das Receitas - Soma das Despesas), mudando a cor do texto para verde (positivo) ou vermelho (negativo) dinamicamente.
*   **Internacionalização de Formatos (Locale)**: O app detecta e adapta dinamicamente as moedas e separadores decimais baseado nas configurações do dispositivo (ex: `R$ 1.250,00` em português e `$ 1,250.00` em inglês).

---

## 🛠️ Tecnologias Utilizadas

*   **Linguagem**: Kotlin 2.0
*   **Interface (UI)**: XML Nativo (layouts declarativos com Material Design 3 e ViewBinding)
*   **Banco de Dados**: SQLite (utilizando a classe nativa `SQLiteOpenHelper`)
*   **Arquitetura**: MVC (Model-View-Controller)
*   **Compatibilidade**: Android SDK Mínimo: 28 (Android 9.0 Pie) | SDK de Compilação: 36

---

## 📂 Estrutura do Projeto

O código está organizado seguindo a separação de responsabilidade em camadas:

```text
com.example.appcashflow
│
├── MainActivity.kt          # Controller/View da Tela de Cadastro e Edição
├── ListarActivity.kt        # Controller/View da Tela de Extrato/Listagem
│
├── adapter
│   └── ElementListaAdapter.kt # Adapter customizado da ListView para os lançamentos
│
├── database
│   └── DatabaseHandler.kt   # Conexão e operações CRUD locais no SQLite
│
└── entity
    └── Cadastro.kt          # Classe de Dados (Model) das transações
