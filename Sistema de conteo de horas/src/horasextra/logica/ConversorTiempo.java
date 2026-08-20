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
        int horas = minutos / 60;
        int mins = minutos % 60;
        if (minutos<0){
            totalHoras[0] = 0;
        } else{
            totalHoras[0] = 1;
        }
        totalHoras[1] = horas;
        totalHoras[2] = mins;

        return totalHoras;
    }
}
