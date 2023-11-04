public class Vetor {
    private int array[];
    private int tamanho;
    public Vetor(int tamanho, int array[]) {
        this.tamanho = tamanho;
        this.array = array;
    }

    public void trocarElementos(int a, int b) {
        int tmp = array[a];
        array[a] = array[b];
        array[b] = tmp;
    }

    public int get(int i) {
        return this.array[i];
    }

    public void set(int i, int v) {
        array[i] = v;
    }

    public int getTamanho() {
        return this.tamanho;
    }

    public void imprimir() {
        for (int i = 0; i < tamanho-1; i++) {
            System.out.print(array[i] + ", ");
        }
        System.out.println(array[tamanho-1]);
    }
}
