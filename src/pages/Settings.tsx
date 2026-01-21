import { useMutation } from "@tanstack/react-query";
import { Input as InputShadCn } from "@/components/ui/input"
import { useState } from "react";
import { compressImage } from "@/lib/utils";
import axios from "axios";
import Button from "@/components/Button";
import useAuthContext from "@/hooks/useAuthContext";

const Settings = () => {
  const [image, setImage] = useState<any>("")
  const [processingRequest, setProcessingRequest] = useState(false)
  const { user } = useAuthContext()

  const { mutate: uploadImage } = useMutation({
    mutationFn: async (accountId) => {
      const formData = new FormData();
      const compressedImage = await compressImage(image)
      formData.append("file", compressedImage)
      return axios({
        method: "post",
        url: `/api/accounts/images/store/${accountId}`,
        data: formData
      })
        .then(res => {
          window.location.reload()
          setProcessingRequest(false)
          return res.data
        })
        .catch(err => {
          setProcessingRequest(false)
          console.log(err)
        })
    }
  })

  async function onImageChange(e: any) {
    const imageFile = e.target.files[0]
    setImage(imageFile)
  }

  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    setProcessingRequest(true)
    uploadImage(user.accountId)
  }


  return (
    <section className="padding-x padding-t pb-20">
      <section className="max-container font-inter">

        <h1 className="font-medium text-2xl text-white mb-20">Settings</h1>

        <form onSubmit={onSubmit} className="">
          <p className="font-medium text-[18px] text-white mb-4">Upload profile image</p>
          <div className="flex items-start gap-4 flex-col">
            <InputShadCn id="picture" accept="image/*" required type="file" onChange={onImageChange} className="max-w-[300px] file:text-[16px] file:text-black text-neutral-700 !text-[16px] !items-center rounded-[8px] outline-0 border-0 bg-input-bg" />
            <Button text={processingRequest ? "Uploading image" : "Save"} disabled={processingRequest} submit={true} icon={true} onClick={() => { }} long={false} />
          </div>
        </form>



      </section>
    </section>
  );
}

export default Settings;
