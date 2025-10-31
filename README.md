# CodCoz Mobile 📱

<div align="center">

![Android](https://img.shields.io/badge/Android-31+-green.svg)
![Java](https://img.shields.io/badge/Java-11-orange.svg)
![License](https://img.shields.io/badge/License-Proprietary-red.svg)

**Aplicativo mobile para gerenciamento de estoque em cozinhas industriais**

[Funcionalidades](#-funcionalidades) • [Instalação](#-instalação) • [Arquitetura](#-arquitetura) • [Tecnologias](#-tecnologias)

</div>

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura](#-arquitetura)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Requisitos](#-requisitos)
- [Instalação](#-instalação)
- [Configuração](#-configuração)
- [Como Executar](#-como-executar)
- [APIs e Serviços](#-apis-e-serviços)
- [Documentação de Funcionalidades](#-documentação-de-funcionalidades)
- [Contribuindo](#-contribuindo)
- [Licença](#-licença)

---

## 🎯 Sobre o Projeto

O **CodCoz Mobile** é um aplicativo Android desenvolvido para gerenciamento completo de estoque e cardápio em cozinhas industriais. O aplicativo permite que estoquistas realizem controle de entrada e baixa de produtos, visualizem cardápios semanais, gerenciem tarefas e monitorem o estoque de forma eficiente.

### Objetivos Principais

- ✅ Gerenciamento de estoque em tempo real
- ✅ Controle de entrada e baixa de produtos via leitura de código de barras
- ✅ Visualização e consulta de cardápios semanais
- ✅ Gerenciamento de tarefas e auditorias
- ✅ Histórico completo de movimentações
- ✅ Monitoramento de estoque baixo e produtos próximos à validade
- ✅ Perfil personalizado do usuário com histórico de atividades

---

## 🚀 Funcionalidades

### 🔐 Autenticação e Segurança
- **Login com Firebase Authentication**: Autenticação segura usando Firebase Auth
- **Cadastro de Usuários**: Cadastro de novos estoquistas via API
- **Recuperação de Senha**: Recuperação de senha via e-mail
- **Validação de Status**: Verificação de status ativo do usuário
- **Cache de Dados**: Armazenamento local de dados do usuário para acesso offline

### 📦 Gerenciamento de Estoque

#### Início
- **Gráfico de Ocupação do Estoque**: Visualização em pizza do percentual de ocupação
- **Indicadores de Status**: 
  - 🟢 Verde: >= 70% (Excelente)
  - 🟠 Laranja: 40-69% (Atenção)
  - 🔴 Vermelho: < 40% (Crítico)
- **Cards Informativos**:
  - Total de produtos em estoque
  - Produtos com estoque baixo
  - Produtos próximos à validade
- **Calendário Interativo**: Visualização de tarefas por data
- **Lista de Tarefas Pendentes**: RecyclerView com estado de loading

#### Entrada de Produtos
- **Scan de Código de Barras**: Leitura automática via câmera
- **Entrada Manual**: Opção para entrada manual de produtos
- **Registro de Quantidade**: Definição de quantidade na entrada
- **Validação de Produtos**: Verificação de existência do produto na API

#### Baixa de Produtos
- **Scan de Código de Barras**: Leitura automática para baixa
- **Baixa Manual**: Opção para baixa manual
- **Confirmação de Quantidade**: Validação antes de confirmar baixa
- **Auditoria de Quantidade**: Sistema de auditoria com bottom sheet

### 📅 Gerenciamento de Cardápio

#### Cardápio Semanal
- **Visualização Expandida**: Lista expansível por dia da semana
- **Estrutura Completa**:
  - Almoço: Arroz, feijão, proteínas, saladas, guarnição, molho, sobremesa
  - Lanche da Manhã: Opções, frutas, opções fixas
  - Lanche da Tarde: Opções, frutas, opções fixas
- **Fallback Automático**: Uso de dados mockados quando API retorna vazio
- **Suporte a Múltiplos Formatos**: Compatibilidade com camelCase e snake_case

#### Receitas
- **Lista de Receitas**: Visualização de todas as receitas disponíveis
- **Busca de Receitas**: Filtro por nome em tempo real
- **Detalhes da Receita**:
  - Ingredientes e quantidades
  - Modo de preparo passo a passo
  - Tempo de preparo e cozimento
  - Porções (com geração aleatória se null)
  - Imagem da receita

### ✅ Gerenciamento de Tarefas

#### Visualização de Tarefas
- **Calendário**: Visualização de tarefas por data
- **Códigos de Cores**:
  - 🔵 Azul: Tarefas pendentes
  - 🟢 Verde: Tarefas concluídas
  - 🔴 Vermelho: Tarefas vencidas
- **Filtro de Tarefas**: Apenas tarefas pendentes na lista principal
- **Detalhes da Tarefa**: Informações completas via bottom sheet

#### Tipos de Tarefas
- **Atividades**: Tarefas gerais do estoquista
- **Conferência de Estoque**: Auditorias e conferências
- **Filtro por Período**: 7, 30 ou 90 dias
- **Filtro por Data de Conclusão**: Filtragem precisa usando dataConclusao

### 📊 Histórico de Movimentações

#### Funcionalidades
- **Lista Completa**: Todas as movimentações de entrada e baixa
- **Filtros Avançados**:
  - Por tipo (Entrada, Baixa, Todos)
  - Por período (Hoje, Ontem, 7 dias, 15 dias, 30 dias, Todos)
  - Por busca (nome do produto)
- **Ordenação**:
  - Mais recentes primeiro
  - Mais antigos primeiro
- **Estado de Loading**: Indicador durante carregamento
- **Estado Vazio**: Mensagens personalizadas quando não há dados

### 👤 Perfil do Usuário

#### Informações do Perfil
- **Dados Pessoais**: Nome completo, função, data de contratação
- **Foto de Perfil**: Upload via Cloudinary
- **Visualização Fullscreen**: Visualização ampliada da foto

#### Histórico de Atividades
- **Abas**: Alternância entre Atividades e Auditorias
- **Filtros por Período**: 7, 30 ou 90 dias
- **Filtro por Data de Conclusão**: Garantia de filtragem precisa
- **Visualização Detalhada**: Tipo de tarefa, código e data

### 🔍 Produtos

#### Visualização
- **Lista de Produtos**: Todos os produtos em estoque
- **Filtros Específicos**:
  - Estoque baixo
  - Próximos à validade
- **Busca**: Filtro em tempo real por nome
- **Estado de Loading**: Indicadores durante carregamento
- **Empty State**: Mensagens quando não há produtos

---

## 🛠 Tecnologias Utilizadas

### Core Android
- **Android SDK**: 31+ (Android 12+)
- **Java**: Versão 11
- **Kotlin DSL**: Build scripts modernos
- **Gradle**: Sistema de build

### Arquitetura e Padrões
- **MVVM (Model-View-ViewModel)**: Arquitetura recomendada do Android
- **LiveData**: Reatividade e observação de dados
- **ViewModel**: Gerenciamento de estado relacionado à UI
- **Repository Pattern**: Abstração de fontes de dados
- **ViewBinding**: Bindings type-safe para views

### Bibliotecas de UI
- **Material Design Components**: Componentes Material Design
- **MPAndroidChart**: Gráficos e visualizações (PieChart)
- **Calendar View** (kizitonwose): Calendário customizado
- **Glide**: Carregamento e cache de imagens
- **RecyclerView**: Listas otimizadas
- **Bottom Sheets**: Diálogos modernos

### Networking
- **Retrofit 2**: Cliente HTTP para APIs REST
- **OkHttp**: Cliente HTTP subjacente
- **Gson**: Serialização/deserialização JSON
- **HttpLoggingInterceptor**: Logging de requisições HTTP

### Autenticação e Backend
- **Firebase Authentication**: Autenticação de usuários
- **Firebase Firestore**: Banco de dados NoSQL
- **Firebase Analytics**: Análise de uso
- **Firebase Cloud Messaging**: Notificações push

### Serviços Externos
- **Cloudinary**: Upload e gerenciamento de imagens
- **ZXing**: Biblioteca de código de barras
- **ZXing Android Embedded**: Integração de scanner

### Armazenamento Local
- **SharedPreferences**: Cache de dados do usuário
- **UserDataManager**: Gerenciador centralizado de dados

### Utilitários
- **EmptyStateAdapter**: Adapter para estados vazios e loading
- **FirebaseHelper**: Helpers para Firebase
- **ImagePickerManager**: Gerenciamento de seleção de imagens
- **CardapioMockData**: Dados mockados para testes

---

## 🏗 Arquitetura

### Padrão MVVM

```
┌─────────────┐
│    View     │  (Fragment, Activity)
│  (UI Layer) │
└──────┬──────┘
       │ Observa
       ▼
┌─────────────┐
│  ViewModel   │  (Lógica de negócio, estado)
│ (Logic Layer)│
└──────┬──────┘
       │ Usa
       ▼
┌─────────────┐
│   Service   │  (API, Firebase)
│ (Data Layer)│
└─────────────┘
```

### Estrutura de Camadas

1. **Presentation Layer**
   - Activities (`actions/`)
   - Fragments (`ui/`)
   - Adapters (`api/adapter/`)
   - ViewBinding

2. **Domain Layer**
   - ViewModels (`ui/*/`)
   - Models (`api/model/`)

3. **Data Layer**
   - Services (`api/service/`)
   - Clients (`api/client/`)
   - Endpoints (`api/endpoints/`)
   - Firebase Helpers (`utils/`)

4. **Infrastructure**
   - Utils (`utils/`)
   - Managers (`utils/`)

---

## 📁 Estrutura do Projeto

```
codcoz-mobile/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/sustria/codcoz/
│   │   │   │   ├── actions/              # Activities específicas
│   │   │   │   │   ├── BaixaScanActivity.java
│   │   │   │   │   ├── EntradaScanActivity.java
│   │   │   │   │   ├── CardapioSemanal.java
│   │   │   │   │   ├── DetalhesReceitaActivity.java
│   │   │   │   │   ├── PerfilActivity.java
│   │   │   │   │   ├── ProdutosActivity.java
│   │   │   │   │   └── ...
│   │   │   │   ├── api/
│   │   │   │   │   ├── adapter/           # RecyclerView Adapters
│   │   │   │   │   ├── client/             # Retrofit Clients
│   │   │   │   │   │   ├── RetrofitClient.java       # API PostgreSQL
│   │   │   │   │   │   ├── RetrofitClientNoSql.java  # API MongoDB
│   │   │   │   │   │   └── RetrofitClientRedis.java  # API Redis
│   │   │   │   │   ├── endpoints/          # Interfaces Retrofit
│   │   │   │   │   ├── model/             # Modelos de dados
│   │   │   │   │   └── service/            # Serviços de API
│   │   │   │   ├── auth/                  # Autenticação
│   │   │   │   │   ├── LoginActivity.java
│   │   │   │   │   ├── CadastroActivity.java
│   │   │   │   │   ├── EsqueceuSenhaActivity.java
│   │   │   │   │   └── SplashScreenActivity.java
│   │   │   │   ├── ui/                    # Fragments e ViewModels
│   │   │   │   │   ├── inicio/
│   │   │   │   │   │   ├── InicioFragment.java
│   │   │   │   │   │   └── InicioViewModel.java
│   │   │   │   │   ├── cardapio/
│   │   │   │   │   │   └── CardapioFragment.java
│   │   │   │   │   ├── historico/
│   │   │   │   │   │   ├── HistoricoFragment.java
│   │   │   │   │   │   └── HistoricoViewModel.java
│   │   │   │   │   └── perfil/
│   │   │   │   │       └── PerfilViewModel.java
│   │   │   │   ├── utils/                 # Utilitários
│   │   │   │   │   ├── UserDataManager.java
│   │   │   │   │   ├── FirebaseHelper.java
│   │   │   │   │   ├── CloudinaryManager.java
│   │   │   │   │   ├── EmptyStateAdapter.java
│   │   │   │   │   ├── ImagePickerManager.java
│   │   │   │   │   └── CardapioMockData.java
│   │   │   │   └── MainActivity.java
│   │   │   ├── res/                       # Recursos Android
│   │   │   │   ├── layout/               # XML layouts
│   │   │   │   ├── values/               # Strings, cores, dimens
│   │   │   │   ├── drawable/             # Imagens e ícones
│   │   │   │   ├── navigation/           # Navigation Graph
│   │   │   │   └── menu/                 # Menus
│   │   │   └── AndroidManifest.xml
│   │   └── test/                         # Testes unitários
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
├── .gradle/
├── .idea/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
├── README.md
└── LICENSE
```

### Principais Pacotes

#### `actions/`
Contém todas as Activities que representam ações específicas:
- **Scan Activities**: Leitura de código de barras
- **Detail Activities**: Visualização de detalhes
- **Bottom Sheets**: Diálogos modernos
- **Filter Activities**: Telas de filtros

#### `api/`
Toda a camada de comunicação com APIs:
- **adapters/**: Adapters para RecyclerView
- **client/**: Clientes Retrofit configurados
- **endpoints/**: Interfaces Retrofit
- **model/**: Modelos de dados (Response/Request)
- **service/**: Serviços que encapsulam chamadas API

#### `ui/`
Fragments e ViewModels seguindo MVVM:
- Cada feature tem seu próprio pacote
- Fragment para UI
- ViewModel para lógica de negócio

#### `utils/`
Classes utilitárias e helpers:
- Gerenciadores de dados
- Helpers de serviços externos
- Adapters customizados
- Dados mockados para testes

---

## 📋 Requisitos

### Sistema
- **Android Studio**: Hedgehog (2023.1.1) ou superior
- **JDK**: 11 ou superior
- **Android SDK**: 
  - `compileSdk`: 36
  - `minSdk`: 31 (Android 12+)
  - `targetSdk`: 36

### Dispositivo
- **Android**: 12.0 (API 31) ou superior
- **Câmera**: Obrigatória (para scanner de código de barras)
- **Conexão Internet**: Requerida para comunicação com APIs

### Permissões
- **INTERNET**: Comunicação com APIs
- **CAMERA**: Scanner de código de barras
- **READ_EXTERNAL_STORAGE**: Seleção de imagens (Android < 13)
- **READ_MEDIA_IMAGES**: Seleção de imagens (Android 13+)

---

## 💻 Instalação

### 1. Clonar o Repositório

```bash
git clone <repository-url>
cd codcoz-mobile
```

### 2. Configurar Firebase

1. Crie um projeto no [Firebase Console](https://console.firebase.google.com/)
2. Adicione um app Android com package name: `com.sustria.codcoz`
3. Baixe o arquivo `google-services.json`
4. Coloque em `app/google-services.json`

### 3. Configurar Cloudinary (Opcional)

1. Crie uma conta no [Cloudinary](https://cloudinary.com/)
2. Obtenha suas credenciais (Cloud Name, API Key, API Secret)
3. Configure em `CloudinaryManager.java` se necessário

### 4. Configurar Variáveis de Ambiente

Edite o arquivo `local.properties` se necessário para configurações específicas do ambiente de desenvolvimento.

---

## ⚙️ Configuração

### URLs das APIs

As URLs das APIs estão configuradas nos clients Retrofit:

#### RetrofitClient (PostgreSQL)
```java
BASE_URL = "https://codcoz-api-postgres.koyeb.app/"
```
- **Endpoints**: Produtos, Tarefas, Histórico, Estoque, Estoquista

#### RetrofitClientNoSql (MongoDB)
```java
BASE_URL = "https://codcoz-api-nosql.koyeb.app/"
```
- **Endpoints**: Receitas, Cardápios

#### RetrofitClientRedis
```java
BASE_URL = "https://codcoz-api-redis.koyeb.app/"
```
- **Endpoints**: Cache e dados temporários

### Configuração de Build

O projeto usa **View Binding** habilitado por padrão:
```kotlin
buildFeatures {
    viewBinding = true
}
```

---

## ▶️ Como Executar

### Via Android Studio

1. Abra o projeto no Android Studio
2. Aguarde a sincronização do Gradle
3. Conecte um dispositivo Android ou inicie um emulador
4. Clique em **Run** (▶️) ou pressione `Shift + F10`
5. Selecione o dispositivo/emulador
6. Aguarde a instalação e execução

### Via Linha de Comando

```bash
# Windows
gradlew.bat installDebug

# Linux/Mac
./gradlew installDebug

# Com logcat
adb logcat | grep CodCoz
```

### Build de Release

```bash
./gradlew assembleRelease
```

O APK será gerado em: `app/build/outputs/apk/release/`

---

## 🔌 APIs e Serviços

### Endpoints Disponíveis

#### API PostgreSQL (RetrofitClient)

**Produtos**
- `GET /produto/listar/estoque/{id_empresa}` - Lista todos os produtos
- `GET /produto/listar/estoque-baixo/{id_empresa}` - Lista produtos com estoque baixo
- `GET /produto/listar/proximo-validade/{id_empresa}` - Lista próximos à validade
- `GET /produto/buscar/{cod_ean}` - Busca produto por código EAN
- `GET /produto/quantidade-estoque/{id_empresa}` - Quantidade total em estoque
- `PUT /produto/entrada/{cod_ean}?quantidade={qtd}` - Registrar entrada
- `PUT /produto/baixa/{cod_ean}?quantidade={qtd}` - Registrar baixa

**Tarefas**
- `GET /tarefa/buscar-data/{email}?data={data}` - Buscar por data
- `GET /tarefa/buscar-periodo/{email}?data_inicio={inicio}&data_fim={fim}` - Buscar por período
- `GET /tarefa/buscar-por-tipo/{email}?data_inicio={inicio}&data_fim={fim}&tipo={tipo}` - Buscar por tipo
- `PUT /tarefa/finalizar-tarefa/{id}` - Finalizar tarefa

**Histórico**
- `GET /historico/{email}` - Listar histórico completo
- `POST /historico/baixa` - Registrar baixa no histórico

**Estoque**
- `GET /estoque/porcentagem/{id_empresa}` - Calcular porcentagem de ocupação

**Estoquista**
- `GET /estoquista/buscar/{email}` - Buscar estoquista por email

#### API MongoDB (RetrofitClientNoSql)

**Receitas**
- `GET /api/v1/empresa/{empresaId}/receita` - Listar receitas

**Cardápios**
- `GET /api/v1/empresa/{empresaId}/cardapio` - Listar cardápios semanais

### Formato de Dados

#### Cardápio
```json
{
  "data_inicio": "2025-10-27",
  "data_fim": "2025-10-31",
  "empresa_id": 1,
  "nomeCardapio": "Cardápio Semanal - 27/10/2025 a 31/10/2025",
  "periodicidade": "Semanal",
  "cardapio_semanal": [
    {
      "diaSemana": "Segunda-feira",
      "data": "2025-10-27",
      "almoco": { ... },
      "lanche_manha": { ... },
      "lanche_tarde": { ... }
    }
  ]
}
```

#### Tarefa
```json
{
  "id": 1,
  "tipoTarefa": "Atividade",
  "ingrediente": "Arroz",
  "situacao": "Pendente",
  "dataConclusao": "2025-10-10",
  "dataLimite": "2025-10-15"
}
```

---

## 📖 Documentação de Funcionalidades

### 🔍 Scanner de Código de Barras

O aplicativo utiliza a biblioteca **ZXing** para leitura de código de barras:

1. **Solicita permissão da câmera**
2. **Abre scanner em tela cheia**
3. **Decodifica automaticamente**
4. **Busca produto na API**
5. **Exibe detalhes para confirmação**

**Fluxo de Entrada**:
```
Scan → Busca Produto → Confirma Quantidade → Registra Entrada → Atualiza Estoque
```

**Fluxo de Baixa**:
```
Scan → Busca Produto → Confirma Quantidade → Auditoria (se necessário) → Registra Baixa → Atualiza Estoque
```

### 📊 Dashboard de Estoque

O dashboard calcula automaticamente:

- **Percentual de Ocupação**: `(produtos_em_estoque / capacidade_total) * 100`
- **Status Dinâmico**: Cores mudam baseado no percentual
- **Comparação com Dia Anterior**: Armazenado no Firebase
- **Indicadores Visuais**: Cards com contadores em tempo real

### 📅 Sistema de Tarefas

#### Filtros de Período

O filtro de período usa a **data de conclusão** (`dataConclusao`) para filtrar corretamente:

- **7 dias**: Últimos 7 dias incluindo hoje
- **30 dias**: Últimos 30 dias incluindo hoje  
- **90 dias**: Últimos 90 dias incluindo hoje

**Cálculo**:
```java
LocalDate hoje = LocalDate.now();
LocalDate dataInicio = hoje.minusDays(dias - 1); // Inclui hoje + (dias-1) anteriores
```

#### Cores no Calendário
- **Azul**: Tarefas pendentes
- **Verde**: Tarefas concluídas
- **Vermelho**: Tarefas vencidas

### 🍽️ Sistema de Cardápio

#### Estrutura de Dados

Cada cardápio contém:
- **Informações Gerais**: Período, empresa, nome
- **Cardápio Semanal**: Array de dias
  - Cada dia tem: `diaSemana`, `data`, `almoco`, `lanche_manha`, `lanche_tarde`

#### Fallback Automático

Quando a API retorna 200 mas com dados vazios, o app automaticamente:
1. Detecta dados vazios
2. Usa dados mockados (`CardapioMockData`)
3. Exibe cardápio de exemplo para não deixar tela vazia

### 🎨 Estados de Loading

Todas as RecyclerViews implementam estados:
- **Loading**: ProgressBar com texto "Carregando..."
- **Empty**: Mensagem personalizada com ícone
- **Content**: Lista normal de itens

### 🔄 Gerenciamento de Dados

#### UserDataManager
- Cache local de dados do usuário
- Persistência via SharedPreferences
- Sincronização com Firebase Firestore
- Dados disponíveis offline

#### EmptyStateAdapter
- Wrapper para RecyclerView adapters
- Gerencia estados vazios e loading
- Mensagens customizáveis
- Sem necessidade de múltiplas views

---

## 🧪 Dados Mockados

O projeto inclui dados mockados para desenvolvimento e testes:

### CardapioMockData

Classe utilitária com cardápios completos:
- 2 cardápios semanais completos
- 5 dias por semana (Segunda a Sexta)
- Todas as refeições estruturadas
- IDs mockados similares aos reais

**Uso**:
```java
// No CardapioSemanal.java
// Descomente para usar dados mockados:
// carregarCardapiosMockados();
```

### Porções Aleatórias

Quando `porcoes` é `null` em receitas:
- Gera número aleatório entre 0 e 10
- Valor persistido para consistência
- Exibido automaticamente na UI

---

## 🎨 Design e UX

### Temas e Cores

- **Material Design 3**: Componentes modernos
- **Cores Dinâmicas**: Cores que mudam baseado no status
- **Dark Mode**: Suporte (se configurado)
- **Edge-to-Edge**: Experiência imersiva

### Componentes Reutilizáveis

- **Headers**: Headers padronizados para Activities
- **Bottom Sheets**: Diálogos modernos e não intrusivos
- **Empty States**: Estados vazios consistentes
- **Loading States**: Indicadores de carregamento padronizados

---

## 🔧 Troubleshooting

### Problemas Comuns

#### Erro ao fazer login
- Verifique conexão com internet
- Confirme que Firebase está configurado
- Verifique se o email existe no sistema

#### Scanner não funciona
- Verifique permissão da câmera
- Teste em dispositivo real (emulador pode não ter câmera)
- Confirme que o código de barras é válido

#### Dados não carregam
- Verifique conexão com APIs
- Confirme URLs das APIs nos clients
- Verifique logs no Logcat

#### Cardápio vazio
- App usa dados mockados automaticamente
- Verifique se empresa_id está correto
- Confirme formato de dados da API

---

## 📝 Convenções de Código

### Nomenclatura
- **Classes**: PascalCase (`ProdutoAdapter`, `CardapioService`)
- **Métodos**: camelCase (`carregarReceitas()`, `setupRecyclerView()`)
- **Variáveis**: camelCase (`tarefaAdapter`, `emptyStateAdapter`)
- **Constantes**: UPPER_SNAKE_CASE (`BASE_URL`, `TYPE_EMPTY`)

### Estrutura de Arquivos
- **Activities**: Sufixo `Activity` (`LoginActivity`, `PerfilActivity`)
- **Fragments**: Sufixo `Fragment` (`InicioFragment`, `CardapioFragment`)
- **ViewModels**: Sufixo `ViewModel` (`InicioViewModel`, `HistoricoViewModel`)
- **Adapters**: Sufixo `Adapter` (`ReceitaAdapter`, `TarefaAdapter`)
- **Services**: Sufixo `Service` (`ProdutoService`, `TarefaService`)
- **Models**: Sufixo `Response` (`TarefaResponse`, `ProdutoResponse`)

### Comentários
- Comentários em português
- Documentação Javadoc para métodos públicos
- Explicações para lógicas complexas

---

## 🧩 Integrações

### Firebase
- **Authentication**: Login e autenticação
- **Firestore**: Armazenamento de dados do usuário
- **Analytics**: Análise de uso (se habilitado)
- **Cloud Messaging**: Notificações push (se configurado)

### Cloudinary
- **Upload de Imagens**: Fotos de perfil
- **Transformações**: Otimização automática
- **CDN**: Distribuição global de imagens

### APIs REST
- **Retrofit**: Cliente HTTP moderno
- **Gson**: Serialização JSON
- **OkHttp**: Interceptors e logging

---

## 🚦 Status do Projeto

### ✅ Funcionalidades Implementadas
- ✅ Autenticação completa
- ✅ Gerenciamento de estoque
- ✅ Scanner de código de barras
- ✅ Visualização de cardápios
- ✅ Gerenciamento de tarefas
- ✅ Histórico de movimentações
- ✅ Perfil de usuário
- ✅ Dashboard de estoque
- ✅ Estados de loading
- ✅ Dados mockados

### 🔄 Em Desenvolvimento
- ⏳ Melhorias de UX
- ⏳ Otimizações de performance
- ⏳ Testes automatizados

### 📋 Planejado
- 📌 Notificações push
- 📌 Modo offline completo
- 📌 Exportação de relatórios
- 📌 Sincronização em tempo real

---

## 👥 Contribuindo

### Processo de Contribuição

1. **Fork** o projeto
2. **Crie** uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** suas mudanças (`git commit -m 'feat: Adiciona nova funcionalidade'`)
4. **Push** para a branch (`git push origin feature/AmazingFeature`)
5. **Abra** um Pull Request

### Convenções de Commit

Seguimos [Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` Nova funcionalidade
- `fix:` Correção de bug
- `docs:` Documentação
- `style:` Formatação
- `refactor:` Refatoração
- `test:` Testes
- `chore:` Manutenção

### Checklist de Pull Request

- [ ] Código segue o padrão do projeto
- [ ] Comentários adicionados onde necessário
- [ ] Documentação atualizada
- [ ] Testes realizados
- [ ] Sem erros de lint
- [ ] Funciona em diferentes dispositivos

---

## 📄 Licença

Este projeto é proprietário. Todos os direitos reservados.

---

## 📞 Suporte

Para suporte e dúvidas:
- Abra uma **Issue** no repositório
- Entre em contato com a equipe de desenvolvimento

---

## 🙏 Agradecimentos

- Equipe de desenvolvimento Sustria
- Comunidade Android
- Mantenedores das bibliotecas open-source utilizadas

---

<div align="center">

**Desenvolvido com ❤️ pela equipe CodCoz**

[Voltar ao Topo](#codcoz-mobile-)

</div>
