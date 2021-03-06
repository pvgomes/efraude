(ns fraude.controllers.cms)


(defn donate []
  (str "
 <div class=\u200B\"column\"\u200B>\n

 <h2 class=\"title\">Ajude com qualquer quantia</h2>
 <br><br>
 O efraude não investimento de nenhuma empresa, são pessoas que cansaram de ver o brasileiro sendo vítimas de fraudes
  e por conta disso se uniram com o intuito de ajudar o próximo. Qualquer quantia para continuar mantendo o site será bem vinda.
 <br><br>
 <strong>Doe bitcoins para a carteira</strong>
<input type=\"text\" class=\"input\" value=\"bc1q58kupk83zjqv5858h23yvldwxjuj5l58xk2s0n\" id=\"myInput\">
<button onclick=\"getbtcwallet()\" class=\"button\">
  <span class=\"icon\">
   <i class=\"fab fa-bitcoin\"></i>
  </span>
  <span>copiar</span>

</button>

 </div>

<script>
function getbtcwallet() {
var copyText = document.getElementById(\"myInput\");
copyText.select();
copyText.setSelectionRange(0, 99999);
 navigator.clipboard.writeText(copyText.value);
 alert(\"copiado: \" + copyText.value);}
</script>
  "))


(defn termos []
  (str             "
                   <div class=​\"column\"​>
                   <h2 class=\"title\">Termos de uso</h2>

                   Esse site é feito para difundir informação às pessoas, concentrando o maior número de golpes e fraudes que estão sendo feitos e criados a cada dia, evitando assim que as pessoas, diante da informação coletada, virem potenciais vítimas e tenham prejuízo financeiro.

                   Acreditamos que com a informação adequada, cada vez mais pessoas tendo acesso, menos vítimas cairão em golpes e fraudes com o tempo.

                   Ajude-nos a ajudar mais pessoas: cadastre o golpe ou a fraude de que você foi vítima. Isso fará um histórico dos golpes e fraudes, em consulta pública, para que outras pessoas não caiam.

                   Juntos vamos combater as tentativas de golpes e fraudes com uma única arma: a informação.
                   <br>
                   <br><br>
                   <strong>Critério adotado de chances para ser fraude</strong>
                   <br>
                   é possível ver 3 tags diferentes em cada fraude sendo, <span class=\"tag is-success\">baixa</span>, <span class=\"tag is-warning\">media</span> e <span class=\"tag is-danger\">alta</span>, elas idicam as chances da fraude denunciada ser de fato fraude.<br>
                   o critério que utilizamos para definir essas tags são pelos votos dos nossos usuários.

                   <br><br>
                   Do uso dos sites sob o domínio efraude.app

                   O eFraude® convida-o a visitar seu site (endereços sob o domínio efraude) e lhe informa os termos e condições que regem o uso do mesmo e de seus serviços disponíveis.

                   Por favor, leia atentamente as condições abaixo estipuladas para que você possa usufruir dos serviços prestados pelo site e lembre-se que ao utilizá-los, você estará declarando ter ciência do presente Termos e Condições de Uso do Site e estará concordando com todas as suas cláusulas e condições.

                   Caso você não concorde com qualquer disposição destes Termos e Condições de Uso, por favor, não utilize nossos serviços.


                   <br><br>
                   <span>
                   <br><br>
                   <strong>Quais os tipos mais comuns de fraudes?</strong>
                   Existem muitas fraudes que as pessoas caem por inocência ou por falta de atenção. Os mais comuns atualmente são:
                   <br><br>
                   <strong>Boletos falsos</strong>
                   Golpistas interceptam a sua correspondência e trocam boletos verdadeiros por falsos, ou simulam sites de fachada para você baixar boletos fraudados. Assim, quando você pagar o boleto, na verdade estará enviando o dinheiro para os golpistas.
                   <br><br>
                   <strong>Roubo de dados em sites falsos</strong>
                   Fraudadores usam sites quase idênticos ao original de lojas famosas para obter seus dados. Pode parecer um erro de senha ou um recadastro. De qualquer forma, preste atenção no link clicado e se for uma loja desconhecida, procure saber mais sobre o e-commerce.
                   <br><br>
                   <strong>Compra de linhas telefônicas</strong>
                   Quando os estelionatários compram uma linha de telefone fixa ou de celular em seu nome para poder ter comprovante de residência para poder cometer outros crimes como:
                   <br><br>
                   <strong>Abertura de empresas</strong>
                   Muitas vezes, os bandidos abrem empresas no nome das vítimas. Elas se transformam em “laranjas”. Com a empresa, os bandidos podem aplicar golpes maiores, pedir empréstimos ou financiamentos em bancos.
                   <br><br>
                   <strong>Pedido de empréstimos com documentos falsificados</strong>
                   Essas são as fraudes mais comuns. E uma das mais perigosas também. Quando um bandido usa seu CPF para pedir empréstimos, ele claramente não vai pagar a parcela. E você acaba ficando com o nome negativado. Muitas vezes só vai saber que isso aconteceu, quando precisa de crédito e ele é negado.
                   </span>

                   </div>
                    "))