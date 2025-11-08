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
import useCreateAccountContext from "@/hooks/useCreateAccountContext";


const AuthStep2 = ({ step, setCurrentStep }: { step: number, currentStep: number, setCurrentStep: any }) => {

  const { userData, setUserDataField } = useCreateAccountContext()
  const [errors, setErrors] = useState<any>([])


  const { mutate } = useMutation({
    mutationFn: async () => {
      return axios({
        method: "post",
        url: "/api/accounts/create/buyer",
        data: {
          userFirstName: userData.userFirstName,
          userLastName: userData.userLastName,
          userLocation: userData.userLocation,
          accountRole: userData.accountRole,
          userContact: userData.userContact,
        }
      })
        .then(res => {
          // window.location.pathname = "/"
          return res.data
        })
        .catch(err => {
          console.log(err)
        })
    }, onSuccess: () => {
      window.location.href = "http://localhost:8080/oauth2/authorization/github";
    }

  })


  const canProgress = () => {
    const inputsFilled = userData.userFirstName !== "" && userData.userLastName !== "" && userData.userLocation !== "" && userData.userContact !== "" && userData.accountRole !== ""
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
          <Input state={userData.userFirstName} setState={setUserDataField("userFirstName")} placeholder="Ime" />
          <Input state={userData.userLastName} setState={setUserDataField("userLastName")} placeholder="Prezime" />
          <Input state={userData.userLocation} setState={setUserDataField("userLocation")} placeholder="Adresa" />
          <Input state={userData.userContact} setState={setUserDataField("userContact")} placeholder="Mobitel" />
          <Select onValueChange={(value: string) => {
            setUserDataField("accountRole")(value)
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
        {userData.accountRole === "trader" &&
          <Button className="min-w-[300px]" long={true} text="Next" icon={true} onClick={nextOnClick} />
        }
        {userData.accountRole !== "trader" &&
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
