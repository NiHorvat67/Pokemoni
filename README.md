# Programsko inženjerstvo TIM 2.2.

test1

> Ime projekta u naslovu ima cilj opisati namjenu projekta te pomoći u podizanju početnog interesa za projekt prezentirajući osnovnu svrhu projekta.
> Isključivo ovisi o Vama!
> 
> Naravno, nijedan predložak nije idealan za sve projekte jer su potrebe i ciljevi različiti. Ne bojte se naglasiti Vaš cilj u ovoj početnoj stranici projekta, podržat ćemo ga bez obzira usredotočili se Vi više na tenologiju ili marketing.
> 
> Zašto ovaj dokument? Samo manji dio timova je do sada propoznao potrebu (a i meni je lakše pratiti Vaš rad).  

# Opis projekta
Ovaj projekt je reultat timskog rada u sklopu projeknog zadatka kolegija [Programsko inženjerstvo](https://www.fer.unizg.hr/predmet/proinz) na Fakultetu elektrotehnike i računarstva Sveučilišta u Zagrebu. 

GearShare je web platforma za iznajmljivanje sezonske sportske opreme koja spaja trgovce i klijente. Trgovci mogu jednostavno oglašavati skije, snowboarde, bicikle, kajake i sličnu opremu, dok klijenti mogu pregledavati ponudu, filtrirati prema potrebama, rezervirati željeni period i obaviti plaćanje.

Motivacija projekta je smanjiti trošak i složenost posjedovanja sezonske opreme te povećati iskorištenost postojeće opreme. Klijentima omogućujemo fleksibilan pristup kvalitetnoj opremi kad im je potrebna, a trgovcima dodatni kanal prodaje i transparentan sustav rezervacija uz izgradnju povjerenja kroz recenzije.

Kroz razvoj projekta cilj nam je savladati moderne obrasce web arhitekture, integraciju vanjskih servisa za autentifikaciju (OAuth 2.0), upravljanje korisničkim ulogama i ovlastima te dobre prakse dokumentiranja, testiranja i timske suradnje.

# Funkcijski zahtjevi
<ul>
<li>Registracija i prijava putem vanjskog pružatelja identiteta.</li>
<li>Podržane uloge i ovlasti: neregistrirani korisnik, klijent, trgovac i administrator.</li>
<li>Javni katalog oglasa s detaljima opreme, cijenom, kaucijom i galerijom.</li>
<li>Interaktivna karta s markerima lokacija preuzimanja i povrata.</li>
<li>Filtriranje po vrsti opreme, razdoblju najma, cijeni i udaljenosti.</li>
<li>Rezervacija željenog perioda uz provjeru dostupnosti u stvarnom vremenu.</li>
<li>Online plaćanje rezervacije karticama i drugim podržanim kanalima.</li>
<li>Godišnja članarina za trgovce s upravljanjem planom i statusom.</li>
<li>Upravljanje oglasima: objava, izmjena, privremena deaktivacija i brisanje bez aktivnih rezervacija.</li>
<li>Ocjene i recenzije nakon završetka najma za izgradnju reputacije.</li>
<li>Prijava nepravilnosti i moderiranje slučajeva od strane administratora.</li>
<li>Administratorski nadzor nad korisnicima, oglasima i transakcijama.</li>
</ul>


# Tehnologije
<ul>
<li>Frontend: React + Vite, statički hostan na Netlify</li>
<li>Backend: Java Spring Boot, deploy na AWS ili Azure</li>
<li>Baza: PostgreSQL, deploy na cloud PaaS (npr. Neon ili ekvivalent)</li>
<li>Autentifikacija: OAuth 2.0 s vanjskim davateljem identiteta</li>
<li>Plaćanja: integracija za kartice i PayPal radi naplate rezervacija i članarina</li>
</ul>


# Članovi tima 
<ul>
  <li><a href="https://github.com/NiHorvat67">Nikola Horvat</a> </li>
  <li><a href="https://github.com/">Leonard Kovač</a></li>
  <li><a href="https://github.com/katic123">Leon Katić</a></li>
  <li><a href="https://github.com/IgorVuk99">Igor Vukovic</a></li>
  <li><a href="https://github.com/SigmaGrindset">Antonio Batarilovic</a></li>
  <li><a href="https://github.com/NoaBasic">Noa Basic</a></li>
  <li><a href="https://github.com/JakovMar">Jakov Marković</a></li>
</ul>

# Kontribucije
>Pravila ovise o organizaciji tima i su često izdvojena u CONTRIBUTING.md



# 📝 Kodeks ponašanja [![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-2.1-4baaaa.svg)](CODE_OF_CONDUCT.md)
Kao studenti sigurno ste upoznati s minimumom prihvatljivog ponašanja definiran u [KODEKS PONAŠANJA STUDENATA FAKULTETA ELEKTROTEHNIKE I RAČUNARSTVA SVEUČILIŠTA U ZAGREBU](https://www.fer.hr/_download/repository/Kodeks_ponasanja_studenata_FER-a_procisceni_tekst_2016%5B1%5D.pdf), te dodatnim naputcima za timski rad na predmetu [Programsko inženjerstvo](https://wwww.fer.hr).
Očekujemo da ćete poštovati [etički kodeks IEEE-a](https://www.ieee.org/about/corporate/governance/p7-8.html) koji ima važnu obrazovnu funkciju sa svrhom postavljanja najviših standarda integriteta, odgovornog ponašanja i etičkog ponašanja u profesionalnim aktivnosti. Time profesionalna zajednica programskih inženjera definira opća načela koja definiranju  moralni karakter, donošenje važnih poslovnih odluka i uspostavljanje jasnih moralnih očekivanja za sve pripadnike zajenice.

Kodeks ponašanja skup je provedivih pravila koja služe za jasnu komunikaciju očekivanja i zahtjeva za rad zajednice/tima. Njime se jasno definiraju obaveze, prava, neprihvatljiva ponašanja te  odgovarajuće posljedice (za razliku od etičkog kodeksa). U ovom repozitoriju dan je jedan od široko prihvačenih kodeks ponašanja za rad u zajednici otvorenog koda.
>### Poboljšajte funkcioniranje tima:
>* definirajte načina na koji će rad biti podijeljen među članovima grupe
>* dogovorite kako će grupa međusobno komunicirati.
>* ne gubite vrijeme na dogovore na koji će grupa rješavati sporove primjenite standarde!
>* implicitno podrazmijevamo da će svi članovi grupe slijediti kodeks ponašanja.
 
>###  Prijava problema
>Najgore što se može dogoditi je da netko šuti kad postoje problemi. Postoji nekoliko stvari koje možete učiniti kako biste najbolje riješili sukobe i probleme:
>* Obratite mi se izravno [e-pošta](mailto:vlado.sruk@fer.hr) i  učinit ćemo sve što je u našoj moći da u punom povjerenju saznamo koje korake trebamo poduzeti kako bismo riješili problem.
>* Razgovarajte s vašim asistentom jer ima najbolji uvid u dinamiku tima. Zajedno ćete saznati kako riješiti sukob i kako izbjeći daljnje utjecanje u vašem radu.
>* Ako se osjećate ugodno neposredno razgovarajte o problemu. Manje incidente trebalo bi rješavati izravno. Odvojite vrijeme i privatno razgovarajte s pogođenim članom tima te vjerujte u iskrenost.

# 📝 Licenca
Važeča (1)
[![CC BY-NC-SA 4.0][cc-by-nc-sa-shield]][cc-by-nc-sa]

Ovaj repozitorij sadrži otvoreni obrazovni sadržaji (eng. Open Educational Resources)  i licenciran je prema pravilima Creative Commons licencije koja omogućava da preuzmete djelo, podijelite ga s drugima uz 
uvjet da navođenja autora, ne upotrebljavate ga u komercijalne svrhe te dijelite pod istim uvjetima [Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License HR][cc-by-nc-sa].
>
> ### Napomena:
>
> Svi paketi distribuiraju se pod vlastitim licencama.
> Svi upotrijebleni materijali  (slike, modeli, animacije, ...) distribuiraju se pod vlastitim licencama.

[![CC BY-NC-SA 4.0][cc-by-nc-sa-image]][cc-by-nc-sa]

[cc-by-nc-sa]: https://creativecommons.org/licenses/by-nc/4.0/deed.hr 
[cc-by-nc-sa-image]: https://licensebuttons.net/l/by-nc-sa/4.0/88x31.png
[cc-by-nc-sa-shield]: https://img.shields.io/badge/License-CC%20BY--NC--SA%204.0-lightgrey.svg

Orginal [![cc0-1.0][cc0-1.0-shield]][cc0-1.0]
>
>COPYING: All the content within this repository is dedicated to the public domain under the CC0 1.0 Universal (CC0 1.0) Public Domain Dedication.
>
[![CC0-1.0][cc0-1.0-image]][cc0-1.0]

[cc0-1.0]: https://creativecommons.org/licenses/by/1.0/deed.en
[cc0-1.0-image]: https://licensebuttons.net/l/by/1.0/88x31.png
[cc0-1.0-shield]: https://img.shields.io/badge/License-CC0--1.0-lightgrey.svg

### Reference na licenciranje repozitorija
