package mx.org.ift.login.captcha.service.impl;

import java.beans.PropertyVetoException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;




import mx.org.ift.login.captcha.constants.Constantes;
import mx.org.ift.login.captcha.service.AudioService;

import javax.sound.sampled.Clip;
import javax.ws.rs.core.Context;


public class AudioServiceImpl {
    
 
	
	public String translate2(String texto){
		String u = new ImageServiceImpl().decrypt(Constantes.TR) + texto;
		try {
			InputStream is = new URL(u).openStream();
			
			BufferedInputStream bis = new BufferedInputStream( is );
			
	        AudioInputStream in = AudioSystem.getAudioInputStream(bis);
	        
	        
	        DataLine.Info info = new DataLine.Info(Clip.class, in.getFormat());
	        
	        Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(in);
            clip.addLineListener(new LineListener()
            {
                public void update(LineEvent event)
                {
                    if (event.getType() == LineEvent.Type.STOP)
                    {
                        event.getLine().close();
                    }
                }
            });
            clip.start();
            
			/*AudioFormat baseFormat = in.getFormat();
			AudioFormat decodedFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
                baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
                false);
			 din = AudioSystem.getAudioInputStream(decodedFormat, in);
		     DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
		     SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
		     if(line != null) {
		    	 line.open(decodedFormat);
		         byte[] data = new byte[4096];
		         // Start
		         line.start();

		         int nBytesRead;
		         while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
		        	 line.write(data, 0, nBytesRead);
		         }
		         // Stop
		         line.drain();
		         line.stop();
		         line.close();
		         din.close();
		     }*/
		     in.close();
		     is.close();
		}catch(MalformedURLException err){
			System.err.println("A:" + err.getMessage());
		}catch(IOException err){
			System.err.println("B:" + err.getMessage());
		}catch(LineUnavailableException err){
			System.err.println("C:" + err.getMessage());
		}catch(UnsupportedAudioFileException err){
			System.err.println("D:" + err.getMessage());
		}
		return u;
	}
	
	
	   public  ByteArrayOutputStream translate(ArrayList operacionList,ServletContext context) throws IOException { 
		   
		   ByteArrayOutputStream bufOut = new ByteArrayOutputStream();
	    	for(int i = 0 ;i<operacionList.size();i++){    		 
	            BufferedInputStream bufIn = 
	                    new BufferedInputStream(context.getResourceAsStream("/audiosClip/"+operacionList.get(i).toString()));
	            byte[] buffer = new byte[1024];
	            int n;
	            while ((n = bufIn.read(buffer)) > 0) {
	                bufOut.write(buffer, 0, n);
	            }	
	    	}
	    	return  bufOut;
	    }
	   
	   public static ArrayList operacionAudios(String operacion){
	    	ArrayList<String> listaAudios = new ArrayList<String>();
	    	Hashtable<String,String> contenedor=new Hashtable<String,String>();
	    	contenedor.put("1","uno.mp3");
	    	contenedor.put("2","dos.mp3");
	    	contenedor.put("3","tres.mp3");
	    	contenedor.put("4","cuatro.mp3");
	    	contenedor.put("5","cinco.mp3");
	    	contenedor.put("6","seis.mp3");
	    	contenedor.put("7","siete.mp3");
	    	contenedor.put("8","ocho.mp3");
	    	contenedor.put("9","nueve.mp3");
	    	contenedor.put("mas","mas.mp3");
	    	contenedor.put("menos","menos.mp3");
	    	contenedor.put("entre","entre.mp3");
	    	contenedor.put("por","por.mp3");
	    	contenedor.put("y","y.mp3");
	    	StringTokenizer tokens = new StringTokenizer(operacion);
	        
	    	System.out.println("longitud" +operacion.length());
	    	
	    	while(tokens.hasMoreTokens()){
	    	
	        	
	    	listaAudios.add(contenedor.get(tokens.nextToken().toString()));
	    	
	    
	    	}
	    	return listaAudios;
	    }
	
	
	
	
	
	/*public String translate(String texto){
		String u = Constantes.TR + texto;
		try {
			URL url = new URL(u);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		    
			conn.setRequestMethod("GET");

			conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "text/plain");
            conn.addRequestProperty("User-Agent", "Mozilla/4.0");
            conn.setRequestProperty("Content-Type", "audio/mpeg");
            conn.setRequestProperty("charset", "UTF-8");
            
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			String output;
			while ((output = br.readLine()) != null) {
				u += output;
			}
			br.close();
			conn.disconnect();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return u;
	}*/
	
/*	public void init(String voiceName) 
		    throws EngineException, AudioException, EngineStateError, 
		           PropertyVetoException {
		if (desc == null) {
		      
			System.setProperty("freetts.voices", 
		        "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
		      
			//Locale locale = new Locale ( "es" , "ES" );
		    desc = new SynthesizerModeDesc(Locale.US);
		    Central.registerEngineCentral
		        ("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
		    synthesizer = Central.createSynthesizer(desc);
		    synthesizer.allocate();
		    synthesizer.resume();
		    SynthesizerModeDesc smd = 
		        (SynthesizerModeDesc)synthesizer.getEngineModeDesc();
		    Voice[] voices = smd.getVoices();
		    Voice voice = null;
		    for(int i = 0; i < voices.length; i++) {
		    	System.out.println(voices[i].getName());
		    	if(voices[i].getName().equals(voiceName)) {
		    		voice = voices[i];
		    		System.out.println(voices[i].getName());
		    		break;
		        }
		    }
		    synthesizer.getSynthesizerProperties().setVoice(voice);
		}
		    
	}
	
	public void terminate() throws EngineException, EngineStateError {
	    synthesizer.deallocate();
	}
	  
	public void doSpeak(String speakText) 
	    throws EngineException, AudioException, IllegalArgumentException, 
	           InterruptedException {
	      synthesizer.speakPlainText(speakText, null);
	      synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);

	}*/
	
	
}
