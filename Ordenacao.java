import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Ordenacao {
    private class Estatisticas {
        public int nInteracoes;
        public int nTrocas;
        public Estatisticas(int i, int t) {
            nInteracoes = i;
            nTrocas = t;
        }
    }

    private Vetor vetor;
    private Estatisticas bubbleSortE, selectionSortE;
    private Estatisticas shellSortE, mergeSortE;
    public Ordenacao(int tamanho, int array[]) {
        vetor = new Vetor(tamanho, array);
        bubbleSortE = new Estatisticas(0, 0);
        selectionSortE = new Estatisticas(0, 0);
        shellSortE = new Estatisticas(0, 0);
        mergeSortE = new Estatisticas(0, 0);
    }

    public void bubbleSort() {
        int n = vetor.getTamanho();
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n-i); j++) {
                bubbleSortE.nInteracoes++;
                if (vetor.get(j-1) > vetor.get(j)) {
                    vetor.trocarElementos(j-1, j);
                    bubbleSortE.nTrocas++;
                }
            }
        }
    }

    public void selectionSort() {
        if (vetor.getTamanho() < 2) return;
        /* 1 - Dividir o array em 2 subarrays:
         *       - (a) um array ordenado (que começa com tamanho 0)
         *       - (b) o resto do array desordenado
         *     Essa divisão será feita pela variável `l` que guarda o
         *     primeiro índice do subarray (b)
         * 2 - Achar o menor elemento do subarray (b) e guardar o índice
         * 3 - Trocar o elemento com o elemento em `l`, se necessário
         * 4 - Se `l` apontar para o último elemento do array, o array estará
         *     ordenado
         * 5 - Caso contrário, incrementar `l` e voltar para o passo 2 */
        int n = vetor.getTamanho();
        for (int l = 0; l < n-1; l++) {
            int minI = l+1;
            for (int i = minI; i < n; i++) {
                selectionSortE.nInteracoes++;
                if (vetor.get(i) < vetor.get(minI)) {
                    minI = i;
                }
            }
            if (vetor.get(minI) < vetor.get(l)) {
                vetor.trocarElementos(l, minI);
                selectionSortE.nTrocas++;
            }
        }
    }


    private int h(int x) {
        if (x < 1) return 0;
        if (x == 1) return 1;
        return 3 * h(x-1) + 1;
    }

    private void shell(int incrementos[], int nIncrementos) {
        for (int incr = 0; incr < nIncrementos; incr++) {
            int span = incrementos[incr];
            for (int j = span; j < vetor.getTamanho(); j++) {
                shellSortE.nInteracoes++;
                int y = vetor.get(j);
                int k;
                for (k = j - span; k >= 0 && y < vetor.get(k); k -= span) {
                    shellSortE.nTrocas++;
                    vetor.set(k+span, vetor.get(k));
                }
                shellSortE.nTrocas++;
                vetor.set(k+span, y);
            }
        }
    }

    public void shellSort() {
        int x = 1;
        int n = vetor.getTamanho();
        while (h(x) < n) {
            x++;
        }
        int nIncrementos = x - 2;
        int incrementos[] = new int[nIncrementos];
        for (int i = 0; i < nIncrementos; i++) {
            incrementos[i] = h(nIncrementos - i);
        }
        shell(incrementos, nIncrementos);
    }

    private Vetor merge(Vetor a, Vetor b) {
        if (b == null) return a;
        int nA = a.getTamanho();
        int nB = b.getTamanho();
        int nC = nA + nB;
        Vetor c = new Vetor(nC, new int[nC]);
        int i = 0; /* Para o vetor A */
        int j = 0; /* Para o vetor B */
        int k = 0; /* Para o vetor C */
        while (i < nA || j < nB) {
            mergeSortE.nInteracoes++;
            if (j >= nB || (i < nA && a.get(i) < b.get(j))) {
                c.set(k++, a.get(i++));
                mergeSortE.nTrocas++;
            } else {
                c.set(k++, b.get(j++));
                mergeSortE.nTrocas++;
            }
        }
        return c;
    }

    private Vetor dividir(Vetor a, Vetor b) {
        Vetor mergeA = a, mergeB = b;
        int nA = a.getTamanho();
        if (nA > 1) {
            /* Vetor A_ terá o tamanho maior se nA for ímpar */
            int nA_ = (nA % 2 == 0) ?
                nA / 2 :
                nA / 2 + 1;
            int nB_ = nA / 2;
            int k = 0;
            Vetor a_ = new Vetor(nA_, new int[nA_]);
            for (int i = 0; i < nA_; i++) {
                mergeSortE.nInteracoes++;
                a_.set(i, a.get(k++));
            }
            Vetor b_ = new Vetor(nB_, new int[nB_]);
            for (int i = 0; i < nB_; i++) {
                mergeSortE.nInteracoes++;
                b_.set(i, a.get(k++));
            }
            mergeA = dividir(a_, b_);
        }
        if (b != null) {
            int nB = b.getTamanho();
            if (nB > 1) {
                /* Vetor A_ terá o tamanho maior se nA for ímpar */
                int nA_ = (nB % 2 == 0) ?
                    nB / 2 :
                    nB / 2 + 1;
                int nB_ = nB / 2;
                int k = 0;
                Vetor a_ = new Vetor(nA_, new int[nA_]);
                for (int i = 0; i < nA_; i++) {
                    mergeSortE.nInteracoes++;
                    a_.set(i, b.get(k++));
                }
                Vetor b_ = new Vetor(nB_, new int[nB_]);
                for (int i = 0; i < nB_; i++) {
                    mergeSortE.nInteracoes++;
                    b_.set(i, b.get(k++));
                }
                mergeB = dividir(a_, b_);
            }
        }
        return merge(mergeA, mergeB);
    }

    public void mergeSort() {
        vetor = dividir(vetor, null);
    }

    public void imprimir() {
        vetor.imprimir();
    }

    private static int[] gerarArray(int tamanho, int max) {
        /* Sempre usar a mesma seed (1) */
        int array[] = new Random(1).ints(0, max)
          .distinct()
          .limit(tamanho)
          .toArray();
        return array;
    }

    private static double millisegundos(long inicio, long fim) {
        return (fim - inicio)/1000000.0;
    }

    private static class Retorno {
        public double tempoBubble, tempoSelection;
        public double tempoShell, tempoMerge;
        public int nInteracoesBubble, nInteracoesSelection, nInteracoesShell, nInteracoesMerge;
        public int nTrocasBubble, nTrocasSelection, nTrocasShell, nTrocasMerge;
        public Retorno(double b, double s, double sh, double m,
                int ib, int is, int ish, int im,
                int tb, int ts, int tsh, int tm) {
            tempoBubble = b;
            tempoSelection = s;
            tempoShell = sh;
            tempoMerge = m;
            nInteracoesBubble = ib;
            nInteracoesSelection = is;
            nInteracoesShell = ish;
            nInteracoesMerge = im;
            nTrocasBubble = tb;
            nTrocasSelection = ts;
            nTrocasShell = tsh;
            nTrocasMerge = tm;
        }
    }

    private static Retorno testar(int array[], int tamanho) {
        double tempoBubble, tempoSelection, tempoShell, tempoMerge;
        Estatisticas bubble, selection, shell, merge;
        {
            int a[] = array.clone();
            Ordenacao o = new Ordenacao(tamanho, a);
            long inicio = System.nanoTime();
            o.bubbleSort();
            long fim = System.nanoTime();
            tempoBubble = millisegundos(inicio, fim);
            bubble = o.bubbleSortE;
        }
        {
            int a[] = array.clone();
            Ordenacao o = new Ordenacao(tamanho, a);
            long inicio = System.nanoTime();
            o.selectionSort();
            long fim = System.nanoTime();
            tempoSelection = millisegundos(inicio, fim);
            selection = o.selectionSortE;
        }
        {
            int a[] = array.clone();
            Ordenacao o = new Ordenacao(tamanho, a);
            long inicio = System.nanoTime();
            o.shellSort();
            long fim = System.nanoTime();
            tempoShell = millisegundos(inicio, fim);
            shell = o.shellSortE;
        }
        {
            int a[] = array.clone();
            Ordenacao o = new Ordenacao(tamanho, a);
            long inicio = System.nanoTime();
            o.mergeSort();
            long fim = System.nanoTime();
            tempoMerge = millisegundos(inicio, fim);
            merge = o.mergeSortE;
        }
        return new Retorno(
                tempoBubble, tempoSelection, tempoShell, tempoMerge,
                bubble.nInteracoes, selection.nInteracoes, shell.nInteracoes, shell.nInteracoes,
                bubble.nTrocas, selection.nTrocas, shell.nTrocas, merge.nTrocas);
    }

    public static void main(String args[]) throws IOException {
        int tamanho = Integer.parseInt(args[0]);
        int array[] = Ordenacao.gerarArray(tamanho, Integer.MAX_VALUE);
        Retorno media = new Retorno(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        for (int i = 0; i < 5; i++) {
            Retorno r = Ordenacao.testar(array, tamanho);

            media.tempoBubble += r.tempoBubble;
            media.tempoSelection += r.tempoSelection;
            media.tempoShell += r.tempoShell;
            media.tempoMerge += r.tempoMerge;

            media.nInteracoesBubble = r.nInteracoesBubble;
            media.nInteracoesSelection = r.nInteracoesSelection;
            media.nInteracoesShell = r.nInteracoesShell;
            media.nInteracoesMerge = r.nInteracoesMerge;
            media.nTrocasBubble = r.nTrocasBubble;
            media.nTrocasSelection = r.nTrocasSelection;
            media.nTrocasShell = r.nTrocasShell;
            media.nTrocasMerge = r.nTrocasMerge;
        }
        media.tempoBubble /= 5;
        media.tempoSelection /= 5;
        media.tempoShell /= 5;
        media.tempoMerge /= 5;
        /* Escrever resultados em arquivos */
        FileWriter f;
        String linha;

        f = new FileWriter(new File("resultado-bubble.csv"), true);
        linha = String.join(",",
                       String.valueOf(tamanho),
                       String.valueOf(media.tempoBubble),
                       String.valueOf(media.nInteracoesBubble),
                       String.valueOf(media.nTrocasBubble));
        f.write(linha + "\n");
        f.close();

        f = new FileWriter(new File("resultado-selection.csv"), true);
        linha = String.join(",",
                       String.valueOf(tamanho),
                       String.valueOf(media.tempoSelection),
                       String.valueOf(media.nInteracoesSelection),
                       String.valueOf(media.nTrocasSelection));
        f.write(linha + "\n");
        f.close();

        f = new FileWriter(new File("resultado-shell.csv"), true);
        linha = String.join(",",
                       String.valueOf(tamanho),
                       String.valueOf(media.tempoShell),
                       String.valueOf(media.nInteracoesShell),
                       String.valueOf(media.nTrocasShell));
        f.write(linha + "\n");
        f.close();

        f = new FileWriter(new File("resultado-merge.csv"), true);
        linha = String.join(",",
                       String.valueOf(tamanho),
                       String.valueOf(media.tempoMerge),
                       String.valueOf(media.nInteracoesMerge),
                       String.valueOf(media.nTrocasMerge));
        f.write(linha + "\n");
        f.close();
    }
}
