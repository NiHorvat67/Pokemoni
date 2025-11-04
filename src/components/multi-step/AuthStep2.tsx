import Input from "@/components/Input";
import Button from "@/components/Button";
import { useState } from "react";
import PopupAlert from "../PopupAlert";
import { useMutation } from "@tanstack/react-query";
import axios from "axios";

import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select-dropdown"

import useAuthContext from "@/hooks/useAuthContext";

const AuthStep2 = ({ step, setCurrentStep }: { step: number, currentStep: number, setCurrentStep: any }) => {

  const [firstName, setFirstName] = useState("sigma")
  const [lastName, setLastName] = useState("grindset")
  const [address, setAddress] = useState("jdsaljdslka")
  const [email, setEmail] = useState("dsajdjdsasaldsak@g")
  const [phoneNum, setPhoneNum] = useState("832901890321")
  const [role, setRole] = useState("")
  const [errors, setErrors] = useState<any>([])

  const { dispatch } = useAuthContext()


  const { mutate } = useMutation({
    mutationFn: async () => {
      return axios({
        method: "post",
        url: "/api/accounts/create",
        data: {
          userEmail: email,
          userFirstName: firstName,
          userLastName: lastName,
          userLocation: address,
          accountRole: "trader",
          userContact: phoneNum,
          username: "321x",
          password: "dxsjdsaakldjslakd",
        }
      })
        .then(res => {
          console.log(res.data)
          dispatch({ type: "LOGIN", payload: res.data })
          window.location.pathname = "/"
          return res.data
        })
        .catch(err => {
          console.log("error")
          console.log(err)
        })
    },

  })


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
    if (!canProgress()) {
      console.log("cant finish")
    } else {
      mutate()
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
            <SelectTrigger className="">
              <SelectValue placeholder="Account type" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="trader">Trader</SelectItem>
              <SelectItem value="buyer">Buyer</SelectItem>

            </SelectContent>
          </Select>
        </div>
        {role === "trader" &&
          <Button className="min-w-[300px]" long={true} text="Next" icon={true} onClick={nextOnClick} />
        }
        {role !== "trader" &&
          <Button className="min-w-[300px]" long={true} text="Finish" icon={true} onClick={finishOnClick} />
        }
      </div>
      {errors.length !== 0 &&
        <PopupAlert errors={errors} />
      }
    </div>
  );
}

export default AuthStep2;
