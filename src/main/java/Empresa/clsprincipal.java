/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Empresa;
import GastosEmpre.ClsGastos;
import Productos.clsproducto;
import Prorateo_Facturas.clsporrateo;
import java.util.Scanner;
/**
 *
 * @author Nilder
 */
public class clsprincipal {
    private static Scanner t = new Scanner(System.in);
    public static void main(String[] args) {
        
        System.out.println("ingrese el numero de productos que quiere ingresar");
        int num = t.nextInt();
        t.nextLine();
        clsporrateo prorrateo = new clsporrateo(num);
        ClsGastos gastos = new ClsGastos();
        for (int i = 0; i < num; i++) {
            clsproducto producto = new clsproducto();
            producto.recolectarInformacion();
            prorrateo.agregaVendedorMatriz(producto);
        }
        
        gastos.recolectarInformacion();
        prorrateo.agregaGastos(gastos);
        clsporrateo.ejecutar();
        
    }
    
}
