package br.ufrj.caronae;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class Util {

    public static void expandOrCollapse(final View v, boolean expand) {
        TranslateAnimation anim;
        if (expand) {
            anim = new TranslateAnimation(0.0f, 0.0f, -v.getHeight(), 0.0f);
            v.setVisibility(View.VISIBLE);
        } else {
            anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, -v.getHeight());
            Animation.AnimationListener collapselistener = new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(View.GONE);
                }
            };

            anim.setAnimationListener(collapselistener);
        }

        anim.setDuration(300);
        anim.setInterpolator(new AccelerateInterpolator(0.5f));
        v.startAnimation(anim);
    }

//    public static String getResponseBody(Response response) {
//        BufferedReader reader;
//        StringBuilder sb = new StringBuilder();
//        try {
//            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
//
//            String line;
//
//            try {
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return sb.toString();
//    }

    public static void toast(int msg) {
        Toast.makeText(App.inst(), App.inst().getString(msg), Toast.LENGTH_SHORT).show();
    }

    public static void toast(String msg) {
        Toast.makeText(App.inst(), msg, Toast.LENGTH_SHORT).show();
    }

    public static String[] getZones() {
        return new String[]{"Centro", "Zona Sul", "Zona Oeste", "Zona Norte", "Baixada", "Grande Niterói", "Outros"};
    }

    public static String[] getNeighborhoods(String zone) {
        if (zone.equals("Centro")) {
            return new String[]{"Benfica", "Caju", "Catumbi", "Centro (Bairro)", "Cidade Nova",
                    "Estácio", "Gamboa", "Glória", "Lapa", "Mangueira", "Rio Comprido",
                    "Santa Teresa", "Santo Cristo", "São Cristóvão", "Saúde", "Vasco da Gama"};
        }
        if (zone.equals("Zona Sul")) {
            return new String[]{"Botafogo", "Catete", "Copacabana", "Cosme Velho",
                    "Flamengo", "Gávea", "Humaitá", "Ipanema", "Jardim Botânico", "Lagoa",
                    "Laranjeiras", "Leblon", "Leme", "Rocinha", "São Conrado", "Urca", "Vidigal"};
        }
        if (zone.equals("Zona Oeste")) {
            return new String[]{"Anil", "Bangu", "Barra de Guaratiba",
                    "Barra da Tijuca", "Camorim", "Campo Grande", "Cidade de Deus", "Cosmos",
                    "Curicica", "Deodoro", "Freguesia de Jacarepaguá", "Gardênia Azul", "Gericinó",
                    "Grumari", "Guaratiba", "Inhoaíba", "Itanhangá", "Jacarepaguá",
                    "Jardim Sulacap", "Joá", "Magalhães Bastos", "Paciência", "Padre Miguel",
                    "Pedra de Guaratiba", "Praça Seca ", "Pechincha", "Realengo",
                    "Recreio dos Bandeirantes", "Santa Cruz", "Santíssimo", "Senador Camará",
                    "Senador Vasconcelos", "Sepetiba", "Tanque", "Taquara", "Vargem Grande",
                    "Vargem Pequena", "Vila Militar", "Vila Valqueire"};
        }
        if (zone.equals("Zona Norte")) {
            return new String[]{"Abolição", "Acari", "Água Santa", "Alto da Boa Vista",
                    "Anchieta", "Andaraí", "Bancários", "Barros Filho", "Bento Ribeiro",
                    "Bonsucesso", "Brás de Pina", "Cachambi", "Cacuia", "Campinho", "Cascadura",
                    "Cavalcanti", "Cocotá", "Coelho Neto", "Colégio",
                    "Complexo do Alemão", "Cordovil", "Costa Barros", "Del Castilho", "Encantado",
                    "Engenheiro Leal", "Engenho Novo", "Engenho da Rainha", "Engenho de Dentro",
                    "Freguesia (Ilha do Governador)", "Galeão", "Grajaú", "Guadalupe",
                    "Higienópolis", "Honório Gurgel", "Inhaúma", "Irajá", "Jacarezinho", "Jacaré",
                    "Jardim América", "Jardim Carioca", "Jardim Guanabara", "Lins de Vasconcelos",
                    "Madureira", "Manguinhos", "Maracanã", "Marechal Hermes", "Maria da Graça",
                    "Maré", "Monero", "Méier", "Olaria", "Oswaldo Cruz", "Parada de Lucas",
                    "Parque Colúmbia", "Pavuna", "Penha", "Penha Circular", "Piedade", "Pilares",
                    "Pitangueiras", "Portuguesa", "Praia da Bandeira", "Praça da Bandeira",
                    "Quintino Bocaiuva", "Ramos", "Riachuelo", "Ribeira", "Ricardo de Albuquerque",
                    "Rocha", "Rocha Miranda", "Sampaio", "São Francisco Xavier", "Tauá", "Tijuca",
                    "Todos os Santos", "Tomás Coelho", "Turiaçu", "Vaz Lobo",
                    "Vicente de Carvalho", "Vigário Geral", "Vila Isabel", "Vila Kosmos",
                    "Vila da Penha", "Vista Alegre", "Zumbi"};
        }
        if (zone.equals("Baixada")) {
            return new String[]{"Belford Roxo", "Duque de Caxias", "Guapimirim", "Itaguai",
                    "Japeri", "Magé", "Mesquita", "Nilópolis", "Nova Iguaçu", "Paracambi",
                    "Queimados", "São João de Meriti", "Seropédica"};
        }
        if (zone.equals("Grande Niterói")) {
            return new String[]{"Itaboraí", "Maricá", "Centro (Niterói)",
                    "Região oceânica (Niterói)", "Rio Bonito", "São Gonçalo", "Tanguá"};
        }
        return null;
    }

    public static String[] getAllNeighborhoods(){
        return new String[]{"Benfica", "Caju", "Catumbi", "Centro (Bairro)", "Cidade Nova",
                "Estácio", "Gamboa", "Glória", "Lapa", "Mangueira", "Rio Comprido",
                "Santa Teresa", "Santo Cristo", "São Cristóvão", "Saúde", "Vasco da Gama",
                "Botafogo", "Catete", "Copacabana", "Cosme Velho",
                "Flamengo", "Gávea", "Humaitá", "Ipanema", "Jardim Botânico", "Lagoa",
                "Laranjeiras", "Leblon", "Leme", "Rocinha", "São Conrado", "Urca", "Vidigal",
                "Anil", "Bangu", "Barra de Guaratiba",
                "Barra da Tijuca", "Camorim", "Campo Grande", "Cidade de Deus", "Cosmos",
                "Curicica", "Deodoro", "Freguesia de Jacarepaguá", "Gardênia Azul", "Gericinó",
                "Grumari", "Guaratiba", "Inhoaíba", "Itanhangá", "Jacarepaguá",
                "Jardim Sulacap", "Joá", "Magalhães Bastos", "Paciência", "Padre Miguel",
                "Pedra de Guaratiba", "Praça Seca ", "Pechincha", "Realengo",
                "Recreio dos Bandeirantes", "Santa Cruz", "Santíssimo", "Senador Camará",
                "Senador Vasconcelos", "Sepetiba", "Tanque", "Taquara", "Vargem Grande",
                "Vargem Pequena", "Vila Militar", "Vila Valqueire",
                "Abolição", "Acari", "Água Santa", "Alto da Boa Vista",
                "Anchieta", "Andaraí", "Bancários", "Barros Filho", "Bento Ribeiro",
                "Bonsucesso", "Brás de Pina", "Cachambi", "Cacuia", "Campinho", "Cascadura",
                "Cavalcanti", "Cocotá", "Coelho Neto", "Colégio",
                "Complexo do Alemão", "Cordovil", "Costa Barros", "Del Castilho", "Encantado",
                "Engenheiro Leal", "Engenho Novo", "Engenho da Rainha", "Engenho de Dentro",
                "Freguesia (Ilha do Governador)", "Galeão", "Grajaú", "Guadalupe",
                "Higienópolis", "Honório Gurgel", "Inhaúma", "Irajá", "Jacarezinho", "Jacaré",
                "Jardim América", "Jardim Carioca", "Jardim Guanabara", "Lins de Vasconcelos",
                "Madureira", "Manguinhos", "Maracanã", "Marechal Hermes", "Maria da Graça",
                "Maré", "Monero", "Méier", "Olaria", "Oswaldo Cruz", "Parada de Lucas",
                "Parque Colúmbia", "Pavuna", "Penha", "Penha Circular", "Piedade", "Pilares",
                "Pitangueiras", "Portuguesa", "Praia da Bandeira", "Praça da Bandeira",
                "Quintino Bocaiuva", "Ramos", "Riachuelo", "Ribeira", "Ricardo de Albuquerque",
                "Rocha", "Rocha Miranda", "Sampaio", "São Francisco Xavier", "Tauá", "Tijuca",
                "Todos os Santos", "Tomás Coelho", "Turiaçu", "Vaz Lobo",
                "Vicente de Carvalho", "Vigário Geral", "Vila Isabel", "Vila Kosmos",
                "Vila da Penha", "Vista Alegre", "Zumbi",
                "Belford Roxo", "Duque de Caxias", "Guapimirim", "Itaguai",
                "Japeri", "Magé", "Mesquita", "Nilópolis", "Nova Iguaçu", "Paracambi",
                "Queimados", "São João de Meriti", "Seropédica",
                "Itaboraí", "Maricá", "Centro (Niterói)",
                "Região oceânica (Niterói)", "Rio Bonito", "São Gonçalo", "Tanguá"};
    }

    public static String[] getHubs() {
        return new String[]{"CCMN: Frente", "CCMN: Fundos", "CCS: Frente", "CCS: HUCFF", "CT: Bloco A", "CT: Bloco D", "CT: Bloco H", "EEFD", "Letras", "Reitoria"};
    }

    public static String[] getCenters() {
        return new String[]{"Todos os Centros", "CCMN", "CCS", "CT", "EEFD", "Letras", "Reitoria"};
    }

    public static String[] getCentersWithoutAllCenters() {
        return new String[]{"CCMN", "CCS", "CT", "EEFD", "Letras", "Reitoria"};
    }

    public static String formatTime(String time) {
        String formattedTime = "";
        try {
            Date date = new SimpleDateFormat("HH:mm:ss", Locale.US).parse(time);
            formattedTime = new SimpleDateFormat("HH:mm", Locale.US).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedTime;
    }

    public static String formatBadDateWithYear(String date) {
        String formattedTime = "";
        try {
            Date date2 = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(date);
            formattedTime = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date2);
        } catch (ParseException e) {
            try{
                Date date2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
                formattedTime = new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(date2);
            } catch (ParseException ex) {
                e.printStackTrace();
            }
        }
        return formattedTime;
    }

    public static String formatGoodDateWithoutYear(String date) {
        String formattedTime = "";
        try {
            Date date2 = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(date);
            formattedTime = new SimpleDateFormat("dd/MM", Locale.US).format(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedTime;
    }

    public static String formatBadDateWithoutYear(String date) {
        String formattedTime = "";
        try {
            Date date2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
            formattedTime = new SimpleDateFormat("dd/MM", Locale.US).format(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedTime;
    }

    public static String formatDateRemoveYear(String date) {
        return date.substring(0, 5);
    }

    // Input Date Format: "YYYY-MM-DD"
    public static int getDayFromDate(String date){
        return Integer.parseInt(date.substring(8, 10));
    }

    // Input Date Format: "YYYY-MM-DD"
    // Return DD/MM
    public static String getDayWithMonthFromDate(String date){
        return date.substring(8, 10) + "/" + date.substring(5, 7);
    }

    // Input Date Format: "YYYY-MM-DD"
    public static String getWeekDayFromDate(String dateString){
        int dayOfWeekInt = -1;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(dateString);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            dayOfWeekInt = c.get(Calendar.DAY_OF_WEEK);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String dayOfWeek = "";

        switch (dayOfWeekInt){
            case 0:
                dayOfWeek = "Sábado";
                break;
            case 1:
                dayOfWeek = "Domingo";
                break;
            case 2:
                dayOfWeek = "Segunda-Feira";
                break;
            case 3:
                dayOfWeek = "Terça-Feira";
                break;
            case 4:
                dayOfWeek = "Quarta-Feira";
                break;
            case 5:
                dayOfWeek = "Quinta-Feira";
                break;
            case 6:
                dayOfWeek = "Sexta-Feira";
                break;
        }
        return " - " + dayOfWeek;
    }

    // Input Date Format: "YYYY-MM-DD"
    public static int getDaysBetweenTwoDates(String date1String, String date2String){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date date1 = format.parse(date1String);
            Date date2 = format.parse(date2String);
            long diff = date2.getTime() - date1.getTime();
            return (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String fixBlankSpace(String word) {
        return word.replace(" ", "");
    }

    public static int convertDpToPixel(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static String getHeaderForHttp(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

//        return "Caronae/" + pInfo.versionCode + "(Android; " + Build.VERSION.CODENAME + ";";
        return "Caronae/"
                + Util.getAppVersionName(context)
                + "("
                + Build.BRAND
                + ": "
                + android.os.Build.MODEL
                + "; "
                + "Android: "
                + Build.VERSION.RELEASE
                + ")";
    }

    public static String getAppVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "Version Not Found";
        }
    }

    public static class BlurBuilder {
        private static final float BITMAP_SCALE = 0.4f;
        private static final float BLUR_RADIUS = 24.5f;

        public static Bitmap blur(View v) {
            return blur(v.getContext(), getScreenshot(v));
        }

        public static Bitmap blur(Context ctx, Bitmap image) {
            int width = Math.round(image.getWidth() * BITMAP_SCALE);
            int height = Math.round(image.getHeight() * BITMAP_SCALE);

            Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
            Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

            RenderScript rs = RenderScript.create(ctx);
            ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
            theIntrinsic.setRadius(BLUR_RADIUS);
            theIntrinsic.setInput(tmpIn);
            theIntrinsic.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);

            return outputBitmap;
        }

        private static Bitmap getScreenshot(View v) {
            Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.draw(c);
            return b;
        }
    }
}
