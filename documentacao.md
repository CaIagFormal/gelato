Documento de Requisitos do Sistema
# Super Gelato
## Sistema de supermercado à distância.

Equipe:
- Carlos Eduardo Fonseca Iagnecz

Pato Branco, Setembro de 2025

***

## 1. Introdução
  	
1.1. Equipe
- Carlos Eduardo Fonseca Iagnecz
  
1.2. Descrição do sistema

Super Gelato é um serviço com finalidade de compras à distância criado para o
	imaginário supermercado titular. As atividades principais do sistema consistem
	na compra e entrega dos produtos do mercado.

1.3. Escopo

O sistema **não irá fornecer um sistema de GPS para os entregadores**, mas
	terá ferramentas para configurar o que é vendido para gestores e permitirá
	a entrega de produtos solicitados a partir de um site.
	
1.4. Referências

***

## 2. Perfis de usuário
   
2.1. Cliente

-	O cliente é quem compra e recebe o produto.

2.2. Entregador

-	O entregador entrega as compras até o cliente>

2.3. Gestor

-	O gestor gerencia os produtos disponíveis.

2.4. Administrador

-	Um gestor com o poder de gerenciar clientes e entregadores.
	
***
	
## 3. Requisitos Funcionais

- RF - Login de usuários

	O sistema deve permitir que todos os usuários entrem em contas a partir de uma
	página com o nome ou e-mail junto da senha.

	Prioridade: 
	- [x] Obrigatório 
	- [ ] Desejável 
	- [ ] Opcional


- RF - Cadastrar cliente
	
  O sistema deve permitir que o CLIENTE crie uma conta a partir de uma página
      pedindo seu nome, senha, confirmação de senha, endereço de e-mail, CEP e data de nascimento

	Prioridade: 
	- [x] Obrigatório 
    - [ ] Desejável 
    - [ ] Opcional


- RF - Visualizar produtos favoritos
	
	O sistema deve mostrar para o CLIENTE os produtos marcados como favorito
	em uma categoria separada acima também, para fins de navegação.
	
	Prioridade: 
	- [ ] Obrigatório 
	- [x] Desejável 
	- [ ] Opcional


- RF - Visualizar os produtos disponíveis
	
	O sistema deve permitir que o CLIENTE veja todos os produtos disponibilisados
	para compra, organizados por setores e subsetores (ex.: refrigerantes, lactíneos)
	mostrando o produto como um ícone, nome, preço e mostrar avisos abaixo.
	(ex.: teor alcoólico, contém lactose)
	
	Prioridade: 
	- [x] Obrigatório 
	- [ ] Desejável 
	- [ ] Opcional


- RF - Alteração de preços para discontos e preço base
	
	O sistema deverá mostrar para o CLIENTE o preço original e o preço do desconto atual
	caso o preço base seja alterado apenas tal deve ser mostrado a não ser que um disconto
	se sobreponha.
	
	Prioridade: 
	- [ ] Obrigatório 
	- [x] Desejável 
	- [ ] Opcional


- RF - Página do produto

	O sistema deve mostrar na página sobre o produto o nome, descrição, conteúdos,
	avisos e estoque.
	
	Prioridade: 
	- [x] Obrigatório 
	- [ ] Desejável 
	- [ ] Opcional
	

- RF - Comprar produto

	O sistema deve permitir que o CLIENTE compre o produto na página do mesmo.
	
	Prioridade: 
	- [x] Obrigatório 
	- [ ] Desejável 
	- [ ] Opcional


- RF - Visualizar o carrinho

	O sistema deve permitir que o CLIENTE veja todos os produtos adicionados no 
	carrinho listados pela ordem cronológica.
	
	Prioridade: 
	- [x] Obrigatório 
	- [ ] Desejável 
	- [ ] Opcional


- RF - Remover itens do carrinho

	O sistema deve permitir que o CLIENTE remova itens do seu carrinho a partir
	de um botão na página.
	
	Prioridade: 
	- [x] Obrigatório 
	- [ ] Desejável 
	- [ ] Opcional
	

- RF - Confirmar compra

	O sistema deve permitir que o CLIENTE confirme a compra de todos os itens
	adicionados no carrinho.
	
	Prioridade: 
	- [x] Obrigatório 
	- [ ] Desejável 
	- [ ] Opcional
	

- RF - Confirmar entrega

	O sistema deve permitir que o CLIENTE confirme o recebimento de sua entrega.
	
	Prioridade: 
	- [x] Obrigatório 
	- [ ] Desejável 
	- [ ] Opcional
	

- RF - Mostrar entregas

	O sistema deve permitir que o ENTREGADOR visualize todas as entregas 
	que estejam disponíveis.
	
	Prioridade: 
	- [x] Obrigatório 
	- [ ] Desejável 
	- [ ] Opcional
	
## 4. Requisitos Não Funcionais 
- RNF - Idade
	
	O sistema deve proibir qualquer usuário menor de 18 de se cadastrar
