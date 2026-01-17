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
import Button from "@/components/Button";
import Textarea from "@/components/Textarea";
import Calendar23 from "@/components/calendar-23-dropdown";
import { useMutation, useQuery } from "@tanstack/react-query";
import axios from "axios";
import useAuthContext from "@/hooks/useAuthContext";
import imageCompression from 'browser-image-compression';
import { useNavigate } from "react-router-dom";

type DateRange = {
  from: Date | undefined,
  to: Date | undefined
}

const NewAdvertisement = () => {

  const [heading, setHeading] = useState("")
  const [pickupLocation, setPickupLocation] = useState("")
  const [returnLocation, setReturnLocation] = useState("")
  const [price, setPrice] = useState("")
  const [deposit, setDeposit] = useState("")
  const [category, setCategory] = useState("")
  const [description, setDescription] = useState("")
  const [image, setImage] = useState<any>("")
  const [dateRange, setDateRange] = useState<DateRange>({ from: undefined, to: undefined })

  const { user } = useAuthContext()
  const [errors, setErrors] = useState<any>([])
  const [processingRequest, setProcessingRequest] = useState(false)
  const navigate = useNavigate()


  const { mutate: deleteAdvertisement } = useMutation({
    mutationFn: async (advertisementId) => {
      return axios.post(`/api/advertisements/delete/${advertisementId}`)
        .then(res => {
          return res.data
        })
        .catch(err => console.log(err));
    }
  });



  const { mutate: createAdvertisement } = useMutation({
    mutationFn: async () => {
      return axios({
        method: "post",
        url: "/api/advertisements/create",
        data: {
          advertisementPrice: Number(price),
          advertisementDeposit: deposit ? Number(deposit) : 0,
          advertisementLocationTakeover: pickupLocation,
          advertisementLocationReturn: returnLocation,
          advertisementStart: dateRange.from!.toISOString().substring(0, 10),
          advertisementEnd: dateRange.to!.toISOString().substring(0, 10),
          traderId: user.accountId,
          itemName: heading,
          itemTypeId: Number(category),
          itemDescription: description,
        }
      })
        .then(res => {
          uploadImage(res.data)
          return res.data
        })
        .catch(err => {
          console.log(err)
          setProcessingRequest(false)
        })
    }
  })

  const { mutate: uploadImage } = useMutation({
    mutationFn: async (advertisementId) => {
      const formData = new FormData();
      const compressedImage = await compressImage(image)
      formData.append("file", compressedImage)
      return axios({
        method: "post",
        url: `/api/advertisements/images/store/${advertisementId}`,
        data: formData
      })
        .then(res => {
          navigate(`/advertisement/${advertisementId}`)
          setProcessingRequest(false)
          return res.data
        })
        .catch(err => {
          deleteAdvertisement(advertisementId)
          setProcessingRequest(false)
          console.log(err)
        })
    }
  })


  const { data: categories } = useQuery({
    queryKey: ["categories"],
    queryFn: async () => {
      return axios
        .get("/api/itemtypes/")
        .then(res => {
          return res.data
        })
        .catch(err => {
          console.log(err)
        })
    }
  })

  async function compressImage(imageFile: any) {
    const options = {
      maxSizeMB: 1,
      useWebWorker: true
    }
    try {
      const compressedFileBlob = await imageCompression(imageFile, options);
      const compressedFile = new File(
        [compressedFileBlob],
        imageFile.name,
        { type: compressedFileBlob.type }
      );
      // console.log(compressedFile)
      // console.log(`compressedFile size ${compressedFileBlob.size / 1024 / 1024} MB`);
      // console.log(`compressedFile size ${compressedFile.size / 1024 / 1024} MB`);
      return compressedFile
    } catch (err) {
      console.log(err)
      return imageFile
    }
  }

  async function onImageChange(e: any) {
    const imageFile = e.target.files[0]
    setImage(imageFile)
  }

  const canProgress = () => {
    const inputsFilled = heading !== "" && pickupLocation !== "" && returnLocation !== "" && price !== "" && category !== "" && description !== "" && dateRange.from !== undefined && dateRange.to !== undefined
    if (!inputsFilled) {
      setErrors((prev: string[]) => [...prev, "Fill out all the input fields"])
      setTimeout(() => {
        setErrors([])
      }, 7 * 1000)
    }
    return inputsFilled
  }

  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    setErrors([])
    if (canProgress()) {
      setProcessingRequest(true)
      createAdvertisement()
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
                  {categories?.map((category: any) => (
                    <SelectItem key={category.itemtypeId} value={category.itemtypeId.toString()}>{category.itemtypeName}</SelectItem>
                  ))}

                </SelectContent>
              </Select>
              <InputShadCn id="picture" accept="image/*" required type="file" onChange={onImageChange} className="file:text-[16px] file:text-black text-neutral-700 !text-[16px] !items-center rounded-[8px] outline-0 border-0 bg-input-bg" />
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
          <Button text={processingRequest ? "Creating advertisement" : "Submit"} disabled={processingRequest} submit={true} icon={true} onClick={() => { }} long={true} />


        </form >

      </section >

      {errors.length !== 0 &&
        <PopupAlert errors={errors} />
      }
    </section >
  );
}

export default NewAdvertisement;
