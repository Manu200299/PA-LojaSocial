
sealed class Screen(val route: String) {
    object Register : Screen("register")
    object CheckIn : Screen("check_in")
    object CheckOut : Screen("check_out")
    object Stock : Screen("stock")
    object Donations : Screen("donations")
    object Volunteers : Screen("volunteers")
    object Statistics : Screen("statistics")
    object Language : Screen("language")
    object LoginVolunteer : Screen("LoginVolunteer")
    object RegisterVolunteer : Screen("RegisterVolunteer")
    object Exit : Screen("exit")
}