"use client"

import { Calendar } from "@/components/ui/calendar"

export default function Calendar14({ range, setRange, advertisementStart, advertisementEnd, reservationPeriods }: { range: { from: Date | undefined, to: Date | undefined }, setRange: any, advertisementStart: Date, advertisementEnd: Date, reservationPeriods: { endDate: string, startDate: string, durationInDays: number }[] }) {



  const bookedDates: Date[] = []
  reservationPeriods.forEach(reservationPeriod => {
    const dates = Array.from(
      { length: reservationPeriod.durationInDays },
      (_, i) => {
        const date = new Date(reservationPeriod.startDate)
        date.setDate(date.getDate() + i)
        return date
      }
    )
    bookedDates.push(...dates)
  })

  return (
    <Calendar
      excludeDisabled
      mode="range"
      selected={range}
      defaultMonth={new Date()}
      onSelect={(range) => {
        if (range == undefined) {
          setRange({ from: undefined, to: undefined })
        } else {
          setRange(range)
        }
      }}
      disabled={[
        { before: new Date() },
        { before: advertisementStart },
        { after: advertisementEnd },
        bookedDates
      ]}
      modifiers={{
        booked: bookedDates,
      }}
      modifiersClassNames={{
        booked: "[&>button]:line-through opacity-100",
      }}
      className="rounded-lg border border-neutral-200 shadow-sm dark:border-neutral-800"
    />
  )
}
