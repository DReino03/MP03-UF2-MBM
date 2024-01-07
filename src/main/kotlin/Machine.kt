import java.time.LocalDate
import java.util.*

//Totes aquelles funcions que interactuen amb l'usuari (scanner) no tenen tests unitaris, ja que no es poden testar.

/**
 * Valors multiplicadors per calcular el preu del bitllet segons la zona i llista de monedes i bitllets acceptats.
 */
const val zona2 = 1.3125
const val zona3 = 1.8443
val allowedMoney = listOf(0.05, 0.10, 0.20, 0.50, 1.0, 2.0, 5.0, 10.0, 20.0, 50.0)

/**
 * Crea una lista de tiquets disponibles mitjançant la classe Ticket.
 * @return Llista de tiquets disponibles.
 * @author Daniel Reinosa, Joel Montalvan, Nikita Barbosa
 */
fun createTicketList(): List<Ticket> {
    return listOf(
        Ticket("Bitllet Senzill", 2.40),
        Ticket("T-Casual", 11.35),
        Ticket("T-Usual", 40.00),
        Ticket("T-Familiar", 70.00),
        Ticket("T-Jove", 80.00)
    )
}

/**
 * Efectua les operacions necessàries per processar la compra del bitllet amb un while que s'executa mentre l'usuari no hagi introduït l'import total. Si l'usuari introdueix un import superior al preu del bitllet, es calcula el canvi.
 * @param scanner: per a l'input de l'usuari.
 * @param price: el preu del bitllet.
 * @return Double amb els diners introduïts per l'usuari.
 * @author Daniel Reinosa, Joel Montalvan, Nikita Barbosa
 */
fun payment(scanner: Scanner, price: Double): Double {
    var money = 0.0
    while (money < price) {
        print("Inserta l'import a pagar: ")
        val userMoney = checkDouble(scanner)
        if (isMoneyAllowed(userMoney)) {
            money += userMoney
            println("Import ingressat: $money")
            if (money < price) {
                val remaining = price - money
                println("Import restant: ${"%.2f".format(remaining)}€")
            }
        } else {
            println("Moneda o bitllet no vàlid. Torna a insertar.")
        }
    }
    return money
}


/**
 * Comprova si la moneda o bitllet introduït per l'usuari és vàlid (està a la llista de monedes i bitllets acceptats).
 * @param userMoney: Moneda o bitllet introduït per l'usuari.
 * @return boolean indicant si la moneda o bitllet és vàlid o no.
 * @author Daniel Reinosa, Joel Montalvan, Nikita Barbosa
 */
fun isMoneyAllowed(userMoney: Double): Boolean {
    return allowedMoney.contains(userMoney)
}

/**
 * Calcula el canvi a retornar a l'usuari.
 * @param money: diners introduïts per l'usuari.
 * @param price: preu del bitllet.
 * @return Double amb el canvi a retornar a l'usuari.
 * @author Daniel Reinosa, Joel Montalvan, Nikita Barbosa
 */
fun calculateChange(money: Double, price: Double): Double {
    val canvi = money - price
    println("Recull el teu canvi: ${"%.2f".format(canvi)}€.")
    return canvi
}


/**
 * Printa el tiquet de compra si ho desitja l'usuari.
 * @param completedTickets: llista de tiquets comprats per l'usuari.
 * @param scanner: per a la resposta de l'usuari.
 * @author Daniel Reinosa, Joel Montalvan, Nikita Barbosa
 */
fun printPurchaseReceipt(completedTickets: MutableList<CompletedPurchase>, scanner: Scanner) {
    print("Desitja tiquet de compra en paper? (S/N): ")
    val userAnswer = getYesOrNo(scanner)
    if (userAnswer) {
        println(
            """
        -----------------------------------
        |             Tiquet              |
        -----------------------------------
        Data: ${LocalDate.now()}
        """.trimIndent()
        )
        println()
        for (ticket in completedTickets) {
            ticket.printTicket()
        }
        val totalPrice = calculateTotalPrice(completedTickets)
        println(
            """
            
        Total: ${"%.2f".format(totalPrice)}€
        -----------------------------------
        """.trimIndent()
        )
    }
}

/**
 * Funció per a la selecció del tiquet a comprar fent servir una funció que limita els inputs al rang d'opcions disponibles.
 * @param scanner: per a l'input de l'usuari.
 * @param ticketList: llista de tiquets disponibles.
 * @return Tiquet seleccionat per l'usuari.
 * @author Daniel Reinosa, Joel Montalvan, Nikita Barbosa
 */
fun selectTicket(scanner: Scanner, ticketList: List<Ticket>): Ticket {
    println(
        """
        Benvingut a la màquina de bitllets de metro. Selecciona el títol de transport que vols comprar:
        1. Bitllet senzill
        2. T-Casual
        3. T-Usual
        4. T-Familiar
        5. T-Jove
    """.trimIndent()
    )
    val userTicket = getIntInRange(1, ticketList.size, scanner)
    val selectedTicket = ticketList[userTicket - 1]
    return selectedTicket
}

/**
 * Selecció de la zona fent servir una funció que limita els inputs al rang d'opcions disponibles.
 * @param scanner: per a l'input de l'usuari.
 * @return Int amb la zona seleccionada per l'usuari.
 * @author Daniel Reinosa, Joel Montalvan, Nikita Barbosa
 */
fun selectZone(scanner: Scanner): Int {
    println(
        """
        Selecciona la zona en la que vols viatjar:
        Zona 1
        Zona 2
        Zona 3
    """.trimIndent()
    )
    val userZone = getIntInRange(1, 3, scanner)
    return userZone
}

/**
 * Compra del bitllet. Crida a les funcions selectTicket, selectZone, payment i calculateChange.
 * @param scanner: per a l'input de l'usuari.
 * @param ticketList: llista de tiquets disponibles.
 * @return CompletedPurchase amb el tiquet comprat i la zona seleccionada per l'usuari.
 * @author Daniel Reinosa, Joel Montalvan, Nikita Barbosa
 */
fun purchaseTicket(scanner: Scanner, ticketList: List<Ticket>): CompletedPurchase {
    val userTicket = selectTicket(scanner, ticketList)
    val userZone = selectZone(scanner)
    val price = userTicket.calculatePrice(userZone)
    println("Preu del bitllet: ${"%.2f".format(price)}€.")
    val money = payment(scanner, price)
    calculateChange(money, price)
    return CompletedPurchase(userTicket, userZone)
}


/**
 * Calcula el preu total de la transacció. Recorre la llista de tiquets comprats i suma el preu de cada tiquet amb la funció calculatePrice, que pertany a la classe Ticket.
 * @param purchasedTickets: llista de tiquets comprats per l'usuari.
 * @return Double amb el preu total de la compra.
 */
fun calculateTotalPrice(purchasedTickets: MutableList<CompletedPurchase>): Double {
    var totalPrice = 0.0
    for (ticket in purchasedTickets) {
        totalPrice += ticket.ticket.calculatePrice(ticket.zone)
    }
    return totalPrice
}

/**
 * Crea un carro buit. Això ens permet emmagatzemar els bitllets comprats per l'usuari per poder printar-los al tiquet.
 * @return MutableList<CompletedPurchase> buit.
 * @author Daniel Reinosa, Joel Montalvan, Nikita Barbosa
 */
fun createCart(): MutableList<CompletedPurchase> {
    return mutableListOf()
}

/**
 * Comprova si el carro està ple (3 bitllets).
 * @param cart: llista de tiquets comprats per l'usuari.
 * @return Boolean indicant si el carro està ple o no.
 * @author Daniel Reinosa, Joel Montalvan, Nikita Barbosa
 */
fun isCartFull(cart: MutableList<CompletedPurchase>): Boolean {
    if (cart.size >= 3) {
        println("El carro està ple. No es poden afegir més bitllets.")
        return true
    }
    return false
}

/**
 * Printa els tiquets comprats per l'usuari i finalitza la compra.
 * @param userCart: llista de tiquets comprats per l'usuari.
 * @param scanner: per a l'input de l'usuari.
 * @author Daniel Reinosa, Joel Montalvan, Nikita Barbosa
 */
fun endPurchase(userCart: MutableList<CompletedPurchase>, scanner: Scanner) {
    printPurchaseReceipt(userCart, scanner)
    println("Gràcies per utilitzar la màquina de venda. Adéu!")
    println()
}

/**
 * Funció principal que inicia la màquina. Crida a les funcions createTicketList, initMachine i printTickets.
 * @param scanner: per a l'input de l'usuari
 * @param ticketList: llista de tiquets disponibles.
 * @author Daniel Reinosa, Joel Montalvan, Nikita Barbosa
 */
fun initMachine(scanner: Scanner, ticketList: List<Ticket>) {
    val userCart = createCart()
    var continueShopping = true

    while (continueShopping) {

        val userTicket = purchaseTicket(scanner, ticketList)

        userCart.add(userTicket)
        print("Vols comprar un altre bitllet? (S/N): ")
        val userAnswer = getYesOrNo(scanner)
        if (!userAnswer || isCartFull(userCart)) {
            continueShopping = false
        }
    }
    endPurchase(userCart, scanner)
}


/*
fun stopMachine(scanner: Scanner) {
    val secretnum = "4321"
    if (scanner.nextLine() == secretnum)
    println("Màquina aturada.")
    exitProcess(0)
}
*/