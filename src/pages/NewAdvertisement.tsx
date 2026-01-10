import Input from "@/components/Input";

import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select-dropdown"
import PopupAlert from "@/components/PopupAlert";


import { Input as InputShadCn } from "@/components/ui/input"

import { useState } from "react";
import { categories } from "@/constants";
import Button from "@/components/Button";
import Textarea from "@/components/Textarea";
import Calendar23 from "@/components/calendar-23-dropdown";
import { useMutation } from "@tanstack/react-query";
import axios from "axios";
import useAuthContext from "@/hooks/useAuthContext";

const NewAdvertisement = () => {

  const [heading, setHeading] = useState("")
  const [pickupLocation, setPickupLocation] = useState("")
  const [returnLocation, setReturnLocation] = useState("")
  const [price, setPrice] = useState("")
  const [deposit, setDeposit] = useState("")
  const [category, setCategory] = useState("")
  const [description, setDescription] = useState("")
  const [dateRange, setDateRange] = useState<any>({ from: "", to: "" })

  const { user } = useAuthContext()
  const [errors, setErrors] = useState<any>([])



  const { mutate } = useMutation({
    mutationFn: async () => {
      return axios({
        method: "post",
        url: "/api/advertisements/create",
        data: {
          advertisementPrice: Number(price),
          advertisementDeposit: deposit ? Number(deposit) : 0,
          advertisementLocationTakeover: pickupLocation,
          advertisementLocationReturn: returnLocation,
          advertisementStart: dateRange?.from.toISOString().substring(0, 10),
          advertisementEnd: dateRange?.to.toISOString().substring(0, 10),
          traderId: user.accountId,
          itemName: heading,
          itemTypeId: Number(category),
          itemDescription: description,
          itemImagePath: "/111111",
        }
      })
        .then(res => {
          window.location.href = `/advertisement/${res.data}`
          return res.data
        })
        .catch(err => {
          console.log(err)
        })
    }
  })

  const canProgress = () => {
    const inputsFilled = heading !== "" && pickupLocation !== "" && returnLocation !== "" && price !== "" && category !== "" && description !== "" && dateRange.from !== "" && dateRange.to !== ""
    if (!inputsFilled) {
      setErrors((prev: string[]) => [...prev, "Fill out all the input fields"])
      setTimeout(() => {
        setErrors([])
      }, 7 * 1000)
    }
    return inputsFilled
  }

  const onSubmit = (e) => {
    e.preventDefault()
    setErrors([])
    if (!canProgress()) {
      console.log("cant finish")
    } else {
      mutate()
    }
  }

  return (
    <section className="padding-x padding-t padding-b min-h-[100vh]">
      <section className="max-container font-inter">

        <h1 className="font-medium text-2xl text-white mb-12 sm:mb-16">Create new advertisement</h1>

        <form onSubmit={onSubmit} className="grid max-md:grid-cols-1 grid-cols-[repeat(2,minmax(250px,380px))] gap-x-20 gap-y-10 max-sm:grid-cols-1x">


          <section className="text-white font-medium max-w-[350px] w-full">
            <h2 className="mb-5">Product info</h2>
            <div className="flex flex-col gap-4">
              <Input placeholder="Ad heading" state={heading} setState={setHeading} />
              <Textarea placeholder="Product description" state={description} setState={setDescription} />
              <Select onValueChange={(value: string) => {
                setCategory(value)
              }}>
                <SelectTrigger className="">
                  <SelectValue placeholder="Category" />
                </SelectTrigger>
                <SelectContent>
                  {categories.map((category) => (
                    <SelectItem key={category.id} value={category.id.toString()}>{category.name}</SelectItem>
                  ))}

                </SelectContent>
              </Select>
              <InputShadCn id="picture" type="file" className="file:text-[16px] file:text-black text-neutral-700 !text-[16px] !items-center rounded-[8px] outline-0 border-0 bg-input-bg" />
            </div>
          </section>

          <section className="text-white font-medium max-w-[350px] w-full">
            <h2 className="mb-5">Product info</h2>
            <div className="flex flex-col gap-4">
              <Input placeholder="Pickup location" state={pickupLocation} setState={setPickupLocation} />
              <Input placeholder="Return location" state={returnLocation} setState={setReturnLocation} />
              <Calendar23 range={dateRange} setRange={setDateRange}></Calendar23>
              <Input placeholder="Price" state={price} setState={setPrice} type="number" />
              <Input placeholder="Deposit" state={deposit} setState={setDeposit} type="number" />
            </div>
          </section>
          <Button text="Submit" submit={true} icon={true} onClick={() => { }} long={true} />


        </form >

      </section >

      {errors.length !== 0 &&
        <PopupAlert className="" errors={errors} />
      }
    </section >
  );
}

export default NewAdvertisement;
