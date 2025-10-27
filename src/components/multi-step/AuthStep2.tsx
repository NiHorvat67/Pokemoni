import Input from "@/components/Input";
import Button from "@/components/Button";
import { useState } from "react";
import PopupAlert from "../PopupAlert";

import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select-dropdown"

const AuthStep2 = ({ step, currentStep, setCurrentStep }: { step: number, currentStep: number, setCurrentStep: any }) => {

  const [firstName, setFirstName] = useState("")
  const [lastName, setLastName] = useState("")
  const [address, setAddress] = useState("")
  const [email, setEmail] = useState("")
  const [phoneNum, setPhoneNum] = useState("")
  const [role, setRole] = useState("")
  const [errors, setErrors] = useState<any>([])

  const canProgress = () => {
    // mozda dodatno provjeriti u bazi podataka jeli moguce napraviti usera s trenutnim podatcima
    // npr jeli email unique, jel email postoji....
    const inputsFilled = firstName !== "" && lastName !== "" && address !== "" && email !== "" && phoneNum !== "" && role !== ""
    if (!inputsFilled) {
      setErrors((prev: string[]) => [...prev, "Fill out all the input fields"])
      setTimeout(() => {
        setErrors([])
      }, 7 * 1000)
    }
    return inputsFilled
  }

  const finishOnClick = () => {
    setErrors([])
    console.log(errors)
    if (!canProgress()) {
      console.log("cant finish")
    } else {
      console.log("creating account")
      console.log("account created")
      window.location.pathname = "/"
    }
  }

  const nextOnClick = () => {
    setErrors([])
    if (!canProgress()) {
      console.log("cant progress")
    } else {
      setCurrentStep(step + 1)
    }

  }

  return (
    <div id="step-container" className="step-content flex max-lg:pb-19 flex-col items-center flex-1 md:justify-center md:mt-[-100px]">
      <div>
        <h1 id="heading" className="font-red-hat mb-5 text-white text-[28px] sm:text-[32px] font-medium text-center">Account setup</h1>
        <p className="text-desc font-inter md:mb-23 mb-16 max-w-[470px] text-center">Trebamo nekoliko osnovnih informacija kako bismo dovršili tvoj profil i omogućili sigurno korištenje platforme.</p>
      </div>
      <div className="">

        <div className="mb-16 flex flex-col items-center gap-3">
          <Input state={firstName} setState={setFirstName} placeholder="Ime" />
          <Input state={lastName} setState={setLastName} placeholder="Prezime" />
          <Input state={address} setState={setAddress} placeholder="Email" />
          <Input state={email} setState={setEmail} placeholder="Adresa" />
          <Input state={phoneNum} setState={setPhoneNum} placeholder="Mobitel" />
          <Select onValueChange={(value: string) => {
            setRole(value)
          }}>
            <SelectTrigger className="w-[130px] sm:w-[180px]">
              <SelectValue placeholder="Account type" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="trader">Trader</SelectItem>
              <SelectItem value="buyer">Buyer</SelectItem>

            </SelectContent>
          </Select>
        </div>
        {role === "trader" &&
          <Button long={true} text="Next" icon={true} onClick={nextOnClick} />
        }
        {role !== "trader" &&
          <Button long={true} text="Finish" icon={true} onClick={finishOnClick} />
        }
      </div>
      {errors.length !== 0 &&
        <PopupAlert errors={errors} />
      }
    </div>
  );
}

export default AuthStep2;
