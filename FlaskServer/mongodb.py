import pymongo
from bson.json_util import dumps, loads
import json


myclient = pymongo.MongoClient("mongodb+srv://testUser:testUser@cluster0.pgn3c.mongodb.net/db_disease")
mydb = myclient["db_disease"]
mycol = mydb["Cure"]

def getByNameDisease(string0):
    myquery = { "NameDisease_ENG": string0 }
    mydoc = mycol.find_one(myquery)
    print(mydoc)
    return mydoc
a = getByNameDisease("Tomato Leaf Mold")

