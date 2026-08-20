package horasextra.logica;

public class ConversorTiempo {
    public static int convertirAMinutos(boolean signo, int horas, int minutos){
        int totalMinutos = minutos + (horas*60);
        if (!signo){
            totalMinutos = -totalMinutos;
        }
        return totalMinutos;
    }
    public static int[] convertirAHoras(int minutos){
        int[] totalHoras = new int[3];
        int horas = Math.abs(minutos / 60);
        int mins = Math.abs(minutos % 60);
        int signo;
        if (minutos<0){
            signo = 0;
        } else{
            signo = 1;
        }
        totalHoras[0] = signo;
        totalHoras[1] = horas;
        totalHoras[2] = mins;

        return totalHoras;
    }
}
