import sideImg from "../assets/images/auth.jpg"
import ApiProvider from "@/components/ApiProvider";
import { githubIcon, googleIcon, checkIcon } from "@/assets/icons";
import Input from "@/components/Input";
import Button from "@/components/Button";
import { subscriptionBenefits } from "@/constants";

import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select-dropdown"

const Auth = () => {


  return (
    <>
      <section className="flex lg:bg-linear-to-b from-[#141A17] to-[#121413]">
        <div className="lg:w-[45%] w-[100%] lg:relative absolute z-1">
          <div id="overlay" className="absolute top-0 bottom-0 right-0 left-0 bg-[#121413]/70 lg:bg-[#121413]/50 lg:rounded-r-[10px]"></div>
          <img src={sideImg} alt="image of a kayak" className="h-[100vh] w-full lg:rounded-r-[10px] object-cover object-center" />
        </div>

        <div className="padding-x lg:px-4 max-lg:pt-30 max-lg:h-[100vh] z-2 flex flex-1">
          {/*
          <div className="flex max-lg:pb-19 flex-col items-center flex-1 md:justify-center md:mt-[-200px] justify-between">
            <div>
              <h1 id="heading" className="font-red-hat mb-5 text-white text-[28px] sm:text-[32px] font-medium text-center">Tvoja avantura počinje ovdje</h1>
              <p className="text-desc font-inter md:mb-16 max-w-[470px] text-center">Pronađi savršenu sportsku opremu i kreni istraživati nove staze, planine i valove.</p>
            </div>
            <div className="flex flex-col gap-6">
              <ApiProvider icon={googleIcon} text="Continue with Google" />
              <ApiProvider icon={githubIcon} text="Continue with Github" />
            </div>
          </div> */}

          {/* <div className="flex max-lg:pb-19 flex-col items-center flex-1 md:justify-center md:mt-[-100px]">
            <div>
              <h1 id="heading" className="font-red-hat mb-5 text-white text-[28px] sm:text-[32px] font-medium text-center">Account setup</h1>
              <p className="text-desc font-inter md:mb-23 mb-16 max-w-[470px] text-center">Trebamo nekoliko osnovnih informacija kako bismo dovršili tvoj profil i omogućili sigurno korištenje platforme.</p>
            </div>
            <div>

              <div className="mb-16 flex flex-col gap-3">
                <Input placeholder="Ime" />
                <Input placeholder="Prezime" />
                <Input placeholder="Email" />
                <Input placeholder="Adresa" />
                <Input placeholder="Mobitel" />
                <Select onValueChange={(value: string) => { }}>
                  <SelectTrigger className="w-[130px] sm:w-[180px]">
                    <SelectValue placeholder="Account type" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="trader">Trader</SelectItem>
                    <SelectItem value="buyer">Buyer</SelectItem>

                  </SelectContent>
                </Select>
              </div>
              <Button text="Finish" icon={true} />
            </div>
          </div> */}

          {/*
          <div className="flex max-lg:pb-19 flex-col items-center flex-1 md:justify-center md:mt-[-100px]">
            <div>
              <h1 id="heading" className="font-red-hat mb-5 text-white text-[28px] sm:text-[32px] font-medium text-center">Activate Subscription</h1>
              <p className="text-desc font-inter mb-16 max-w-[470px] text-center">Subscribe now to list your equipment on GearShare and reach customers ready to rent your gear.</p>
            </div>

            <div className="flex flex-col gap-8">
              <div className="flex flex-col gap-4">
                {subscriptionBenefits.map((benefit) => (
                  <div className="flex items-center gap-2 text-white font-inter font-medium">
                    <img src={checkIcon} alt="checkmark icon" />
                    {benefit}
                  </div>
                ))}
              </div>
              <p className="font-inter font-semibold text-white text-[24px]">100€ <span className="font-normal text-base">/ YEAR</span></p>
              <Button text="Proceed to Payment" icon={true} />
            </div>
          </div> */}

          {/* <div className="flex max-lg:pb-19 max-lg:pt-10 flex-col items-center flex-1 md:justify-center md:mt-[-200px] gap-5 lg:gap-5">
            <img src={checkIcon} alt="success icon" className="h-25 w-25" />
            <div>
              <h1 id="heading" className="font-red-hat mb-5 text-white text-[28px] sm:text-[32px] font-medium text-center">Thank you</h1>
              <p className="text-desc font-inter mb-16 max-w-[470px] text-center">Payment processed! You can now start listing equipment.</p>
            </div>
          </div> */}




        </div>

      </section >
    </>

  );
}

export default Auth;
