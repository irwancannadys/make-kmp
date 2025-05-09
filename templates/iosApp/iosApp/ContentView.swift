import SwiftUI
import shared

struct ContentView: View {
    let greeting = Greeting().greet()

    var body: some View {
        Text(greeting)
            .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
