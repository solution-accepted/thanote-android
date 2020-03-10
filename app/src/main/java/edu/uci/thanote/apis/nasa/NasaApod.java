package edu.uci.thanote.apis.nasa;

import com.google.gson.annotations.SerializedName;
import edu.uci.thanote.apis.ImageNote;

/**
 * Created by Joxon on 2020-03-09.
 */
public class NasaApod implements ImageNote {
    private String code;

    @SerializedName("title")
    private String title;

    @SerializedName("explanation")
    private String detail;

    @SerializedName("url")
    private String imageUrl;

    @SerializedName("hdurl")
    private String imageUrlHd;

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return "Nasa: " + title;
    }

    public String getDetail() {
        return detail;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

// https://api.nasa.gov/
// https://apod.nasa.gov/apod/astropix.html

// ERROR
//{
//  "code": 400,
//  "msg": "unconverted data remains: 0",
//  "service_version": "v1"
//}

// NORMAL
//{
//  "copyright": "Roman PončaMasaryk U.",
//  "date": "2020-03-09",
//  "explanation": "What is the band of light connecting the ground to the Milky Way?  Zodiacal light -- a stream of dust that orbits the Sun in the inner Solar System. It is most easily seen just before sunrise, where it has been called a false dawn, or just after sunset. The origin of zodiacal dust remains a topic of research, but is hypothesized to result from asteroid collisions and comet tails.  The featured wide-angle image shows the central band of our Milky Way Galaxy arching across the top, while the Large Magellanic Cloud (LMC), a satellite galaxy to our Milky Way, is visible on the far left. The image is a combination of over 30 exposures taken last July near La Serena among the mountains of Chile.  During the next two months, zodiacal light can appear quite prominent in northern skies just after sunset.   Almost Hyperspace: Random APOD Generator",
//  "hdurl": "https://apod.nasa.gov/apod/image/2003/ZodiacalMw_Ponca_5834.jpg",
//  "media_type": "image",
//  "service_version": "v1",
//  "title": "Milky Way and Zodiacal Light over Chile",
//  "url": "https://apod.nasa.gov/apod/image/2003/ZodiacalMw_Ponca_960.jpg"
//}