/**
 * Representa una compra finalitzada.
 * @param ticket: tiquet comprat.
 * @param zone: zona seleccionada per l'usuari.
 * @author Daniel Reinosa, Joel Montalvan, Nikita Barbosa
 */
class CompletedPurchase (val ticket: Ticket, val zone: Int) {
    /**
     * Imprimeix al tiquet el nom, la zona i el preu.
     */
    fun printTicket() {
        println("${ticket.name} $zone  Preu: ${ticket.calculatePrice(zone)}€")
    }
}