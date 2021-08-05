/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Prorateo_Facturas;
import GastosEmpre.ClsGastos;
import Productos.clsproducto;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author Nilder
 */
public class clsporrateo {
    private static String[][] prorrateo = new String[1][1];
    private static Double[] gastos = new Double[1];
    private static Double[] pesoProductos = new Double[1];
    public static NumberFormat formatoCantidad = NumberFormat.getCurrencyInstance(new Locale(""));
    
    
    private static final int cantidad = 0;
    private static final int descripcion = 1;
    private static final int valor = 2;
    private static final int gasto_al_valor = 3;
    private static final int gastos_po_peso = 4;
    private static final int costo_por_unidad = 5;
    private static final int costosTotales = 6;
    private final int MAX_COLUMNAS = 7;
    
    private int filaActual = 0;
    
    public clsporrateo(int filas){
        prorrateo = new String[filas][MAX_COLUMNAS];
        pesoProductos = new Double[filas];
        gastos = new Double[5];
    }
    
    public static String cambiarFormato(String cantidad){
        Double nuevaCantidad = Double.parseDouble(cantidad);
        return formatoCantidad.format(nuevaCantidad);
    }
    
    public static void imprimirDecorado(){
        for (int x = 0; x < prorrateo.length; x++) { 
            System.out.print("|");
            for (int y = 0; y < prorrateo[x].length; y++) {
                if (y > 1){
                    System.out.print(cambiarFormato(prorrateo[x][y]));
                }
                else{
                    System.out.print(prorrateo[x][y]);
                }
                if (y != prorrateo[x].length - 1) {
                    System.out.print("\t");
                }
            }
            System.out.println("|");
        }
    }
    
    public static Double valorTotal(){
        Double total = 0.0;
        for (int fila = 0; fila < prorrateo.length; fila++) {
            total += (Double.valueOf(prorrateo[fila][valor])*Double.valueOf(prorrateo[fila][cantidad]));
        }
        return total;
    }
    
    public static Double sumarArreglo(int inicio){
        Double total = 0.0;
        for (int i = inicio; i < gastos.length; i+=2) {
            total += gastos[i];
        }
        return total;
    }
    
    public static void gastosValor(Double coeficiente){
        for (int i = 0; i < prorrateo.length; i++) {
            prorrateo[i][gasto_al_valor] = (Double.valueOf(prorrateo[i][valor])*coeficiente) + "";
        }
    }
    
    public static Double pesoTotal(){
        Double total = 0.0;
        for (int fila = 0; fila < prorrateo.length; fila++) {
            total += (Double.valueOf(prorrateo[fila][cantidad])*pesoProductos[fila]);
        }
        return total;
    }
    
    public static Double totalGastoPeso(){
        Double total = 0.0;
        for (int i = 1; i < gastos.length; i++) {
            total += Double.valueOf(gastos[i]);
        }
        return total;
    }
    
    public static void gastosPeso(Double coeficiente){
        for (int fila = 0; fila < prorrateo.length; fila++) {
            prorrateo[fila][gastos_po_peso] = (pesoProductos[fila]*coeficiente) + "";
        }
    }
    
    public static void calculoCostoUnitario(){
        for (int fila = 0; fila < prorrateo.length; fila++) {
            prorrateo[fila][costo_por_unidad] = (Double.valueOf(prorrateo[fila][valor])
                                            +Double.valueOf(prorrateo[fila][gasto_al_valor])
                                            +Double.valueOf(prorrateo[fila][gastos_po_peso])) + "";
        }
    }
    
    public static void calculoCostoTotal(){
        for (int fila = 0; fila < prorrateo.length; fila++) {
            prorrateo[fila][costosTotales] = (Double.valueOf(prorrateo[fila][costo_por_unidad])
                                           *Double.valueOf(prorrateo[fila][cantidad])) + "";
        }
    }
    
    public static Double totalCuadro(){
        Double total = 0.0;
        for (int fila = 0; fila < prorrateo.length; fila++) {
            total += Double.valueOf(prorrateo[fila][costosTotales]);
        }
        return total;
    }
    
    public static String comprobacion(Double a, Double b){
        Double diferencia = Math.abs(a - b);
        if (diferencia <= 0.20){
            
            return "listo";
        }
        else {
            return "error";
        }
    }
    
    public static void ejecutar(){
        
        Double valorTotal = valorTotal();
      
        Double totalGastoValor = sumarArreglo(0);
     
        Double coeficienteGasto = totalGastoValor/valorTotal;
     
        gastosValor(coeficienteGasto);
   
        Double pesoTotal = pesoTotal();
       
        Double totalGastoPeso = sumarArreglo(1);
    
        Double coeficienteGastoPeso = totalGastoPeso/pesoTotal;
        
        gastosPeso(coeficienteGastoPeso);
       
        calculoCostoUnitario();
        calculoCostoTotal();
        imprimirDecorado();
       
        Double totalCuadro = totalCuadro();
        Double granTotal = valorTotal + totalGastoValor + totalGastoPeso;
        String resultado = comprobacion(totalCuadro, granTotal);
        System.out.printf(Locale.UK, resultado, gastos);
        System.out.print("\nEl gran total es: " + granTotal);
    }
    
    public String agregaVendedorMatriz(clsproducto producto){
        if (filaActual >= prorrateo.length){
            return "exeso de filas";
        }
        else{
            prorrateo[filaActual][descripcion] = producto.getDescripcion();
            prorrateo[filaActual][cantidad] = producto.getCantidad() + "";
            prorrateo[filaActual][valor] = producto.getValor() + "";
            pesoProductos[filaActual] = producto.getPeso();
            filaActual++;
        }
        return "OK!";
    }
    
    public void agregaGastos(ClsGastos objGastos){
        gastos[0] = objGastos.getSeguro();
        gastos[1] = objGastos.getFlete();
        gastos[2] = objGastos.getAduana();
        gastos[3] = objGastos.getAcarreo();
        gastos[4] = objGastos.getBanco();
    }
    
}
